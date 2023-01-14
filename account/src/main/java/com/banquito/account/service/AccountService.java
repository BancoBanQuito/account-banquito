package com.banquito.account.service;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banquito.Utils.BankUtils;
import com.banquito.account.config.AccountStatusCode;
import com.banquito.account.config.RSCode;
import com.banquito.account.errors.RSRuntimeException;
import com.banquito.account.model.Account;
import com.banquito.account.model.AccountClient;
import com.banquito.account.model.AccountClientPK;
import com.banquito.account.model.AccountPK;
import com.banquito.account.repository.AccountClientRepository;
import com.banquito.account.repository.AccountRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountClientRepository accountClientRepository;

    private final String ACCOUNT_NOT_CREATED = "Ha ocurrido un error al crear la cuenta";
    private final String NOT_ENOUGH_PARAM = "Faltan parametros en la peticion";

    public AccountService(AccountRepository accountRepository, AccountClientRepository accountClientRepository) {
        this.accountRepository = accountRepository;
        this.accountClientRepository = accountClientRepository;
    }

    @Transactional
    public Account createAccount(Account account, String identification, String identificationType) {
        if (identification.isEmpty() || identificationType.isEmpty() || account.equals(null)) {
            throw new RSRuntimeException(this.NOT_ENOUGH_PARAM, RSCode.NOT_FOUND);
        }

        Date createDate = new Date();
        String localAccountCode = BankUtils.RandomNumber.generateCode(20);
        String internationalAccountCode = BankUtils.RandomNumber.generateCode(34);
        AccountPK accountPK = new AccountPK();
        accountPK.setCodeinternationalaccount(internationalAccountCode);
        accountPK.setCodelocalaccount(localAccountCode);
        account.setPk(accountPK);
        account.setStatus(AccountStatusCode.ACTIVATE.code);
        account.setCreateDate(createDate);

        account.setLastUpdateDate(createDate);
        account.setCloseDate(createDate);
        account.setAvailableBalance(new BigDecimal(0));
        account.setPresentBalance(new BigDecimal(0));

        log.info(account.toString());

        AccountClientPK accountClientPK = new AccountClientPK();
        accountClientPK.setCodeInternationalAccount(internationalAccountCode);
        accountClientPK.setCodeLocalAccount(localAccountCode);
        accountClientPK.setIdentification(identification);
        accountClientPK.setIdentificationType(identificationType);

        AccountClient accountClient = AccountClient.builder()
                .pk(accountClientPK)
                .status(AccountStatusCode.ACTIVATE.code)
                .createDate(createDate)
                .build();
        try {
            this.accountRepository.save(account);
            this.accountClientRepository.save(accountClient);
        } catch (Exception e) {
            throw new RSRuntimeException(this.ACCOUNT_NOT_CREATED, RSCode.INTERNAL_ERROR_SERVER);
        }
        return account;
    }
}