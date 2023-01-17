package com.banquito.account.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.banquito.account.utils.Utils;
import org.springframework.stereotype.Service;

import com.banquito.account.utils.RSCode;
import com.banquito.account.controller.dto.RSAccountStatement;
import com.banquito.account.controller.mapper.AccountStatementMapper;
import com.banquito.account.exception.RSRuntimeException;
import com.banquito.account.model.Account;
import com.banquito.account.model.AccountClient;
import com.banquito.account.model.AccountStatementLog;
import com.banquito.account.model.AccountStatementLogPK;
import com.banquito.account.repository.AccountClientRepository;
import com.banquito.account.repository.AccountStatementLogRepository;

import jakarta.transaction.Transactional;

@Service
public class AccountStatementLogService {

    private final AccountStatementLogRepository accountStatementLogRepository;
    private final AccountClientRepository accountClientRepository;

    private final String NOT_FOUND_ACCOUNT = "Cuenta no encontrada";
    private final String NOT_FOUND_ACCOUNT_STATEMENT = "No existen estados de cuenta";

    public AccountStatementLogService(AccountStatementLogRepository accountStatementLogRepository,
            AccountClientRepository accountClientRepository) {
        this.accountStatementLogRepository = accountStatementLogRepository;
        this.accountClientRepository = accountClientRepository;
    }

    public RSAccountStatement findAccountStatement(String accountCode) {

        /* ----------------- Account Client ----------------- */
        Optional<AccountClient> optionalAccountClient = this.accountClientRepository
                .findByPkCodeLocalAccountOrPkCodeInternationalAccount(accountCode, accountCode);
        if (!optionalAccountClient.isPresent()) {
            throw new RSRuntimeException(this.NOT_FOUND_ACCOUNT, RSCode.NOT_FOUND);
        }
        AccountClient accountClient = optionalAccountClient.get();

        /* ----------------- Client ----------------- */
        String fullname = this.retriveClient(accountClient.getPk().getIdentification(),
                accountClient.getPk().getIdentificationType());

        /* ----------------- Last Account Statement ----------------- */
        Optional<AccountStatementLog> optionalLastAccountStatement = this.accountStatementLogRepository
                .findTopByOrderByCurrentCutOffDateDesc();
        if (!optionalLastAccountStatement.isPresent()) {
            throw new RSRuntimeException(this.NOT_FOUND_ACCOUNT_STATEMENT, RSCode.NOT_FOUND);
        }
        AccountStatementLog lastAccountStatementLog = optionalLastAccountStatement.get();

        /* ----------------- Transactions ----------------- */
        List<RSAccountStatement.Transaction> transactions = optionalLastAccountStatement.isPresent()
                ? this.retriveAllTransactions(accountCode,
                        optionalLastAccountStatement.get().getCurrentCutOffDate(),
                        new Date())
                : this.retriveAllTransactions(accountCode, new Date(), new Date());

        RSAccountStatement rsAccountStatement = AccountStatementMapper.map(fullname, accountClient,
                lastAccountStatementLog, transactions);
        return rsAccountStatement;
    }

    private List<RSAccountStatement.Transaction> retriveAllTransactions(String account, Date from, Date to) {
        List<RSAccountStatement.Transaction> transactions = new ArrayList<>();
        return transactions;
    }

    private String retriveClient(String identification, String identificationType) {
        String fullname = "Juanito Perez";
        return fullname;
    }

    @Transactional
    private AccountStatementLog createAccountStatementLog(Account account, BigDecimal interestRate,
            BigDecimal promBalance,
            String type, Optional<AccountStatementLog> optionalLastAccountStatement) {

        AccountStatementLogPK pk = new AccountStatementLogPK();
        pk.setCodeAccountStateLog(Utils.generateAlphanumericCode(10));
        pk.setCodeInternationalAccount(account.getPk().getCodeInternationalAccount());
        pk.setCodeLocalAccount(account.getPk().getCodeLocalAccount());

        AccountStatementLog accountStatementLog = AccountStatementLog.builder()
                .pk(pk)
                .lastCutOffDate(optionalLastAccountStatement.isPresent()
                        ? optionalLastAccountStatement.get().getCurrentCutOffDate()
                        : new Date())
                .interest(interestRate)
                .currentBalance(account.getPresentBalance())
                .averageCashBalance(promBalance)
                .build();

        return accountStatementLog;
    }

}
