package com.banquito.account.service;

import com.banquito.account.controller.dto.RSSignature;
import com.banquito.account.controller.mapper.AccountSignatureMapper;
import com.banquito.account.exception.RSRuntimeException;
import com.banquito.account.model.Account;
import com.banquito.account.model.AccountSignature;
import com.banquito.account.model.AccountSignaturePK;
import com.banquito.account.repository.AccountRepository;
import com.banquito.account.repository.AccountSignatureRepository;
import com.banquito.account.request.ClientRequest;
import com.banquito.account.request.dto.RSClientSignature;
import com.banquito.account.utils.*;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AccountSignatureService {
    private final AccountSignatureRepository accountSignatureRepository;
    private final AccountRepository accountRepository;
    private final ClientRequest clientRequest;

    public AccountSignatureService(AccountSignatureRepository accountSignatureRepository,
                                   AccountRepository accountRepository,
                                   ClientRequest clientRequest){
        this.accountSignatureRepository = accountSignatureRepository;
        this.accountRepository = accountRepository;
        this.clientRequest = clientRequest;
    }

    @Transactional
    public void createSignature(AccountSignature accountSignature){

        //api call to client module
        RSClientSignature clientData = clientRequest.getClientData(
                accountSignature.getPk().getIdentificationType(), accountSignature.getPk().getIdentification());

        if(clientData == null){
            throw new RSRuntimeException(Messages.CLIENT_NOT_FOUND, RSCode.BAD_REQUEST);
        }

        //account is required to get international code
        Optional<Account> opAccount = accountRepository.findByPkCodeLocalAccount(
                accountSignature.getPk().getCodeLocalAccount());

        if(!opAccount.isPresent()){
            throw new RSRuntimeException(Messages.ACCOUNTS_NOT_FOUND_FOR_CODE, RSCode.NOT_FOUND);
        }

        Account account = opAccount.get();

        AccountSignaturePK tempPk = accountSignature.getPk();
        tempPk.setCodeInternationalAccount(account.getPk().getCodeInternationalAccount());

        //set params
        accountSignature.setPk(tempPk);
        accountSignature.setSignatureReference(clientData.getSignature());
        accountSignature.setCreateDate(Utils.currentDate());
        accountSignature.setStatus(Status.ACTIVATE.code);

        //save record
        try {
            this.accountSignatureRepository.save(accountSignature);
        } catch (Exception e) {
            throw new RSRuntimeException(Messages.SIGNATURE_NOT_CREATED, RSCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void updateSignatureRoleStatus(
            String identificationType,  String identification, String codeLocalAccount, String role, String status){

        Optional<AccountSignature> opAccountSignature = accountSignatureRepository
                .findByPkIdentificationTypeAndPkIdentificationAndPkCodeLocalAccount(
                        identificationType, identification,codeLocalAccount);

        //verified record exist
        if(!opAccountSignature.isPresent()){
            throw new RSRuntimeException(Messages.ACCOUNTS_NOT_FOUND_FOR_CODE, RSCode.NOT_FOUND);
        }

        AccountSignature accountSignature = opAccountSignature.get();
        accountSignature.setRole(role);
        accountSignature.setStatus(status);

        //update record
        try {
            this.accountSignatureRepository.save(accountSignature);
        } catch (Exception e) {
            throw new RSRuntimeException(Messages.SIGNATURE_NOT_UPDATED, RSCode.INTERNAL_SERVER_ERROR);
        }
    }

    public List<RSSignature> findSignaturesById(String identificationType, String identification){

        //get requested record
        List<AccountSignature> dbSignatures = accountSignatureRepository.
                findByPkIdentificationTypeAndPkIdentification(identificationType, identification);

        if(dbSignatures.size() < 1){
            throw new RSRuntimeException(Messages.ACCOUNTS_NOT_FOUND_FOR_CLIENT, RSCode.NOT_FOUND);
        }

        //Call api for client name
        RSClientSignature clientSignature = clientRequest.getClientData(identificationType,identification);

        if(clientSignature == null){
            throw new RSRuntimeException(Messages.CLIENT_NOT_FOUND, RSCode.BAD_REQUEST);
        }

        String name = clientSignature.getName() + " " + clientSignature.getLastName();

        //Format and send data
        List<RSSignature> signatures = new ArrayList<>();
        RSSignature signature;
        for(AccountSignature dbSignature: dbSignatures){
            signature = AccountSignatureMapper.map(dbSignature, name);
            signatures.add(signature);
        }

        return signatures;
    }


    public List<RSSignature> findSignaturesByCode(String codeLocalAccount){

        /*List<AccountSignature> dbSignatures = accountSignatureRepository.findByPkCodeLocalAccountAndPkCodeInternationalAccount(
                codeLocalAccount, codeInternationalAccount);*/

        List<AccountSignature> dbSignatures = accountSignatureRepository.findByPkCodeLocalAccount(codeLocalAccount);

        if(dbSignatures.size() < 1){
            throw new RSRuntimeException(Messages.ACCOUNTS_NOT_FOUND_FOR_CODE, RSCode.NOT_FOUND);
        }

        List<RSSignature> signatures = new ArrayList<>();
        RSSignature signature;
        for(AccountSignature dbSignature: dbSignatures){
            RSClientSignature clientSignature = clientRequest.getClientData(
                    dbSignature.getPk().getIdentificationType(),dbSignature.getPk().getIdentification());

            if(clientSignature == null){
                throw new RSRuntimeException(Messages.CLIENT_NOT_FOUND, RSCode.BAD_REQUEST);
            }

            String name = clientSignature.getName() + " " + clientSignature.getLastName();

            signature = AccountSignatureMapper.map(dbSignature, name);
            signatures.add(signature);
        }

        return signatures;
    }
}
