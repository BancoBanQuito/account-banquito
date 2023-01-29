package com.banquito.account.service;

import com.banquito.account.controller.dto.RSAccountAssociatedService;
import com.banquito.account.controller.mapper.AccountAssociatedServiceMapper;
import com.banquito.account.exception.RSRuntimeException;
import com.banquito.account.model.Account;
import com.banquito.account.model.AccountAssociatedService;
import com.banquito.account.model.AccountAssociatedServicePK;
import com.banquito.account.repository.AccountAssociatedServiceRepository;
import com.banquito.account.repository.AccountRepository;
import com.banquito.account.utils.Messages;
import com.banquito.account.utils.RSCode;
import com.banquito.account.utils.Status;
import com.banquito.account.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AccountAssociatedServiceService {

    private final AccountAssociatedServiceRepository accountAssociatedServiceRepository;

    private final AccountRepository accountRepository;

    public AccountAssociatedServiceService(AccountAssociatedServiceRepository accountAssociatedServiceRepository,
                                           AccountRepository accountRepository){
        this.accountAssociatedServiceRepository =  accountAssociatedServiceRepository;
        this.accountRepository = accountRepository;
    }

    public RSAccountAssociatedService createAssociatedService(AccountAssociatedService accountAssociatedService){

        //account is required to get international code
        Optional<Account> opAccount = accountRepository.findByPkCodeLocalAccount(
                accountAssociatedService.getPk().getCodeLocalAccount());

        if(!opAccount.isPresent()){
            throw new RSRuntimeException(Messages.ACCOUNTS_NOT_FOUND_FOR_CODE, RSCode.NOT_FOUND);
        }

        Account account = opAccount.get();

        AccountAssociatedServicePK tempPk = accountAssociatedService.getPk();
        tempPk.setCodeInternationalAccount(account.getPk().getCodeInternationalAccount());

        //set params
        accountAssociatedService.setPk(tempPk);
        accountAssociatedService.setStatus(Status.ACTIVATE.code);
        accountAssociatedService.setStartDate(Utils.currentDate());

        //save record
        try {
            this.accountAssociatedServiceRepository.save(accountAssociatedService);
        } catch (Exception e) {
            throw new RSRuntimeException(Messages.SERVICE_NOT_CREATED, RSCode.INTERNAL_SERVER_ERROR);
        }

        return AccountAssociatedServiceMapper.map(accountAssociatedService);
    }

    public void updateAssociatedServiceStatus(String codeLocalAccount, String codeAssociatedService, String status){

        Optional<AccountAssociatedService> opService = accountAssociatedServiceRepository.findByPkCodeLocalAccountAndPkCodeAssociatedService(
                codeLocalAccount, codeAssociatedService);

        if(!opService.isPresent()){
            throw new RSRuntimeException(Messages.SERVICE_NOT_FOUND, RSCode.NOT_FOUND);
        }

        AccountAssociatedService accountAssociatedService = opService.get();

        accountAssociatedService.setStatus(status);

        try {
            this.accountAssociatedServiceRepository.save(accountAssociatedService);
        } catch (Exception e) {
            throw new RSRuntimeException(Messages.SERVICE_NOT_UPDATED, RSCode.INTERNAL_SERVER_ERROR);
        }
    }
}
