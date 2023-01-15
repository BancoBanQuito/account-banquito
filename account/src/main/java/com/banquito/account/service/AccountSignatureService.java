package com.banquito.account.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.banquito.account.config.AccountStatusCode;
import com.banquito.account.config.RSCode;
import com.banquito.account.errors.RSRuntimeException;
import com.banquito.account.model.Account;
import com.banquito.account.model.AccountClient;
import com.banquito.account.model.AccountClientPK;
import com.banquito.account.model.AccountSignature;
import com.banquito.account.model.AccountSignaturePK;
import com.banquito.account.repository.AccountClientRepository;
import com.banquito.account.repository.AccountSignatureRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountSignatureService {

    private final AccountSignatureRepository accountSignatureRepository;
    private final AccountClientRepository accountClientRepository;

    private final String ACCOUNT_NOT_CREATED = "Ha ocurrido un error al crear la firma asociada";
    private final String NOT_ENOUGH_PARAM = "Faltan parametros en la peticion";
    private final String NOT_FOUND_ACCOUNT = "Cuenta no encontrada";
    private final String NOT_FOUND_CLIENT_PK = "Cliente no cuenta con una cuenta asociada";

    public AccountSignatureService(AccountSignatureRepository accountSignatureRepository, AccountClientRepository accountClientRepository) {
        this.accountSignatureRepository = accountSignatureRepository;
        this.accountClientRepository = accountClientRepository;
    }

    public AccountSignature createAccountSignature(AccountSignature accountSignature, String identification,
            String identificationType, String codeAccount, String signature) {

        if (accountSignature.equals(null) || identification.isEmpty() || identificationType.isEmpty()
                || codeAccount.isEmpty()) {
            throw new RSRuntimeException(this.NOT_ENOUGH_PARAM, RSCode.NOT_FOUND);
        }
        
        Optional<AccountClient> optionalAccountClient = this.accountClientRepository
        .findByPkCodeLocalAccountOrPkCodeInternationalAccount(codeAccount, codeAccount);

        if (!optionalAccountClient.isPresent()) {
            throw new RSRuntimeException(this.NOT_FOUND_ACCOUNT, RSCode.NOT_FOUND);
        }
        
        boolean existClient = this.accountClientRepository.existsByPkIdentificationAndPkIdentificationType(identification, identificationType);
        

        AccountSignaturePK accountSignaturePK = new AccountSignaturePK(optionalAccountClient.get().getPk().getCodeLocalAccount(),
        optionalAccountClient.get().getPk().getCodeInternationalAccount(),
                identificationType, identification);

    
        List<AccountSignature> accountSignaturePKs = this.accountSignatureRepository
                .findByPk(accountSignaturePK);
// && existClient
        //if (accountSignaturePKs.isEmpty()) {
            accountSignature.setPk(accountSignaturePK);
            accountSignature.setStatus(AccountStatusCode.ACTIVATE.code);
            accountSignature.setSignatureReference(signature);
            accountSignature.setCreateDate(new Date());
            accountSignature.setStartDate(new Date());
            accountSignature.setVersion(1);
       // }
        try{
            log.info(accountSignature.toString());
            this.accountSignatureRepository.save(accountSignature);
        }catch (Exception e) {
            throw new RSRuntimeException(this.ACCOUNT_NOT_CREATED, RSCode.INTERNAL_ERROR_SERVER);
        }
        log.info(accountSignature.toString());
        return accountSignature;
    }
}
