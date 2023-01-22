package com.banquito.account.service;

import com.banquito.account.controller.dto.RSAccountStatement;
import com.banquito.account.controller.dto.RSAccountStatementTransactions;
import com.banquito.account.controller.mapper.AccountStatementMapper;
import com.banquito.account.exception.RSRuntimeException;
import com.banquito.account.model.Account;
import com.banquito.account.model.AccountPK;
import com.banquito.account.model.AccountStatementLog;
import com.banquito.account.model.AccountStatementLogPK;
import com.banquito.account.repository.AccountRepository;
import com.banquito.account.repository.AccountStatementLogRepository;
import com.banquito.account.request.TransactionRequest;
import com.banquito.account.request.dto.RSInterest;
import com.banquito.account.request.dto.RSTransaction;
import com.banquito.account.utils.Messages;
import com.banquito.account.utils.RSCode;
import com.banquito.account.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AccountStatementLogService {

    private final AccountStatementLogRepository accountStatementLogRepository;
    private final AccountRepository accountRepository;

    public AccountStatementLogService(AccountStatementLogRepository accountStatementLogRepository,
            AccountRepository accountRepository) {
        this.accountStatementLogRepository = accountStatementLogRepository;
        this.accountRepository = accountRepository;
    }

    public RSAccountStatement findAccountStatement(String codeLocalAccount, String codeInternationalAccount) {

        AccountPK pk = new AccountPK();
        pk.setCodeLocalAccount(codeLocalAccount);
        pk.setCodeInternationalAccount(codeInternationalAccount);

        Optional<Account> opAccount = accountRepository.findById(pk);

        if(!opAccount.isPresent()){
            throw new RSRuntimeException(Messages.ACCOUNTS_NOT_FOUND_FOR_CODE, RSCode.NOT_FOUND);
        }

        Account account = opAccount.get();

        AccountStatementLog accountStatementLog = getAccountStatement(account);
        RSAccountStatement accountStatement = AccountStatementMapper.map(accountStatementLog);
        List<RSAccountStatementTransactions> statementTransactions = new ArrayList<>();
        RSAccountStatementTransactions statementTransaction;

        List<RSTransaction> transactions = TransactionRequest.getTransactionsBetweenDates(
                account.getPk().getCodeLocalAccount(),
                LocalDateTime.ofInstant(accountStatementLog.getLastCutOffDate().toInstant(), ZoneId.systemDefault()),
                LocalDateTime.ofInstant(accountStatementLog.getCurrentCutOffDate().toInstant(), ZoneId.systemDefault())
        );

        if(transactions.size() < 1){
            throw new RSRuntimeException(Messages.TRANSACTIONS_NOT_FOUND, RSCode.NOT_FOUND);
        }

        for(RSTransaction transaction: transactions){
            statementTransaction = AccountStatementMapper.map(transaction);
            statementTransactions.add(statementTransaction);
        }

        accountStatement.setTransactions(statementTransactions);

        try {
            this.accountStatementLogRepository.save(accountStatementLog);

        } catch (Exception e) {
            throw new RSRuntimeException(Messages.STATEMENT_NOT_CREATED, RSCode.INTERNAL_SERVER_ERROR);
        }

        return accountStatement;
    }


    public AccountStatementLog getAccountStatement(Account account) {
        Date tempLastCutOffDate;
        LocalDateTime lastCutOffDate;
        LocalDateTime currentCutOffDate = Utils.currentDateTime();

        BigDecimal previousBalance;
        BigDecimal credit = BigDecimal.valueOf(0);
        BigDecimal debit = BigDecimal.valueOf(0);
        BigDecimal interest = BigDecimal.valueOf(0);
        BigDecimal currentBalance;
        BigDecimal averageBalance;
        BigDecimal tempAverageBalance = BigDecimal.valueOf(0);


        List<AccountStatementLog> logs = accountStatementLogRepository.findByPkCodeLocalAccountOrderByCurrentCutOffDateDesc(
                account.getPk().getCodeLocalAccount()
        );

        if(logs.size() > 0){
            tempLastCutOffDate = logs.get(0).getCurrentCutOffDate();
            previousBalance = logs.get(0).getCurrentBalance();
        }else {
            tempLastCutOffDate = account.getCreateDate();
            previousBalance = BigDecimal.valueOf(0);
        }

        lastCutOffDate =  LocalDateTime.ofInstant(tempLastCutOffDate.toInstant(), ZoneId.systemDefault());

        List<RSTransaction> transactions = TransactionRequest.getTransactionsBetweenDates(
                account.getPk().getCodeLocalAccount(),
                lastCutOffDate,
                currentCutOffDate
        );

        List<RSInterest> dailyBalances = TransactionRequest.getInterestBetweenDates(
                account.getPk().getCodeLocalAccount(),
                lastCutOffDate,
                currentCutOffDate
        );

        if(transactions.size() > 0){
            for(RSTransaction transaction: transactions){
                if(transaction.getMovement().equals("NOTA DEBITO")){
                    debit = debit.add(transaction.getValue());
                }

                if(transaction.getMovement().equals("NOTA CREDITO")) {
                    credit = credit.add(transaction.getValue());
                }

                if(transaction.getType().equals("INTERES")){
                    interest = interest.add(transaction.getValue());
                }
            }
        }

        if(dailyBalances.size() > 0){
            for(RSInterest dailyBalance: dailyBalances){
                tempAverageBalance = tempAverageBalance.add(dailyBalance.getAvailableBalance());
            }
        }

        averageBalance = tempAverageBalance.divide(BigDecimal.valueOf(30),4, RoundingMode.HALF_EVEN);

        averageBalance = averageBalance.setScale(2, RoundingMode.HALF_EVEN);

        currentBalance = account.getAvailableBalance();

        AccountStatementLogPK pk = new AccountStatementLogPK();
        pk.setCodeAccountStateLog(Utils.generateAlphanumericCode(10));
        pk.setCodeLocalAccount(account.getPk().getCodeLocalAccount());
        pk.setCodeInternationalAccount(account.getPk().getCodeInternationalAccount());

        AccountStatementLog accountStatementLog = AccountStatementLog.builder()
                .pk(pk)
                .currentCutOffDate(Utils.currentDate())
                .lastCutOffDate(tempLastCutOffDate)
                .previousBalance(previousBalance)
                .creditMovements(credit)
                .debitMovements(debit)
                .interest(interest)
                .currentBalance(currentBalance)
                .averageCashBalance(averageBalance)
                .build();

        return  accountStatementLog;
    }
}
