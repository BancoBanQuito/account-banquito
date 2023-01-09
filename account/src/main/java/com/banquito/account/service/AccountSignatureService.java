package com.banquito.account.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banquito.account.model.AccountSignature;
import com.banquito.account.repository.AccountRepository;
import com.banquito.account.repository.AccountSignatureRepository;

@Service
public class AccountSignatureService {
    private final AccountSignatureRepository accountSignatureRepository;
    private final AccountRepository accountRepository;
    // private final ClientRepository clientRepository;

    // add client repository
    public AccountSignatureService(AccountSignatureRepository accountSignatureRepository,
            AccountRepository accountRepository) {
        this.accountSignatureRepository = accountSignatureRepository;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void createAccountSignature(AccountSignature accountSignature) {

    }

    @Transactional
    public void cancelAccountSignature(String codeAccount, String identification) {

    }

    //must return a dto object
    public List<AccountSignature> findByAccountCode(String local, String international){
         return null;
    }
}
