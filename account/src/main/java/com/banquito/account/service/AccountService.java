package com.banquito.account.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banquito.account.model.Account;
import com.banquito.account.model.AccountSignature;
import com.banquito.account.model.AccountStatementLog;
import com.banquito.account.repository.AccountAssociatedServiceRepository;
import com.banquito.account.repository.AccountClientRepository;
import com.banquito.account.repository.AccountRepository;
import com.banquito.account.repository.AccountSignatureRepository;
import com.banquito.account.repository.AccountStatementLogRepository;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountAssociatedServiceRepository accountAssociatedServiceRepository;
    private final AccountClientRepository accountClientRepository;
    private final AccountSignatureRepository accountSignatureRepository;
    private final AccountStatementLogRepository accountStatementLogRepository;

    public AccountService(AccountRepository accountRepository,
            AccountAssociatedServiceRepository accountAssociatedServiceRepository,
            AccountClientRepository accountClientRepository, AccountSignatureRepository accountSignatureRepository,
            AccountStatementLogRepository accountStatementLogRepository) {
        this.accountRepository = accountRepository;
        this.accountAssociatedServiceRepository = accountAssociatedServiceRepository;
        this.accountClientRepository = accountClientRepository;
        this.accountSignatureRepository = accountSignatureRepository;
        this.accountStatementLogRepository = accountStatementLogRepository;
    }

    @Transactional
    public void createAccount(Account account) {
    }

    @Transactional
    public void suspendAccount(String accountCode) {
    }

    @Transactional
    public void updateAccountBalance(String accountCode, BigDecimal balance) {
    }

    @Transactional
    public void inactiveAccount(String accountCode) {
    }

    public AccountStatementLog getAccountStatement(String accountCode) {
        return null;
    }

    public AccountSignature getAssociatedSignature(String accountCode) {
        return null;
    }

    @Transactional
    public void createAssociatedSignature(String accountCode, AccountSignature signature) {
    }

    @Transactional
    public void suspendAssociatedSignature(String accountCode, String signatureCode) {
    }

    public List<Account> getConsolidatedPosition(String identificationType, String identification) {
        return null;
    }

    public List<Account> getClientAccounts(String identificationType, String identification) {
        return null;
    }
}