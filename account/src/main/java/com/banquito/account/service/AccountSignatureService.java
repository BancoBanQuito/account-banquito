package com.banquito.account.service;

import com.banquito.account.controller.dto.RSSignature;
import com.banquito.account.controller.mapper.AccountSignatureMapper;
import com.banquito.account.exception.RSRuntimeException;
import com.banquito.account.model.AccountSignature;
import com.banquito.account.model.AccountSignaturePK;
import com.banquito.account.repository.AccountSignatureRepository;
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

    public AccountSignatureService(AccountSignatureRepository accountSignatureRepository){
        this.accountSignatureRepository = accountSignatureRepository;
    }

    //function for development purposes
    public Object Test(){

        return "Hello fellow humans";
    }


    @Transactional
    public void createSignature(AccountSignature accountSignature){

        //api call to client module
        accountSignature.setSignatureReference("https://static.cdn.wisestamp.com/wp-content/uploads/2020/08/Oprah-Winfrey-Signature-1.png");

        //set missing params
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
    public void updateSignatureRoleStatus(SignatureUpdate signature){

        //get requested record
        Optional<AccountSignature> opAccountSignature = accountSignatureRepository.findById(
                AccountSignaturePK.builder()
                        .identificationType(signature.getIdentificationType())
                        .identification(signature.getIdentification())
                        .codeInternationalAccount(signature.getCodeInternationalAccount())
                        .codeLocalAccount(signature.getCodeLocalAccount())
                        .build()
        );

        //verified record exist
        if(!opAccountSignature.isPresent()){
            throw new RSRuntimeException(Messages.ACCOUNTS_NOT_FOUND_FOR_CODE, RSCode.NOT_FOUND);
        }

        AccountSignature accountSignature = opAccountSignature.get();
        accountSignature.setRole(signature.getRole());
        accountSignature.setStatus(signature.getStatus());

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

        //Call api for client name TODO

        //Format and send data
        List<RSSignature> signatures = new ArrayList<>();
        RSSignature signature;
        for(AccountSignature dbSignature: dbSignatures){
            signature = AccountSignatureMapper.map(dbSignature, "Nombre Cliente");
            signatures.add(signature);
        }

        return signatures;
    }


}
