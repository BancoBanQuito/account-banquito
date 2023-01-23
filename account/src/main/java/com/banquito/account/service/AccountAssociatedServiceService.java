package com.banquito.account.service;

import com.banquito.account.controller.dto.RSAccountAssociatedService;
import com.banquito.account.controller.mapper.AccountAssociatedServiceMapper;
import com.banquito.account.exception.RSRuntimeException;
import com.banquito.account.model.AccountAssociatedService;
import com.banquito.account.repository.AccountAssociatedServiceRepository;
import com.banquito.account.utils.Messages;
import com.banquito.account.utils.RSCode;
import com.banquito.account.utils.Status;
import com.banquito.account.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AccountAssociatedServiceService {

    private final AccountAssociatedServiceRepository accountAssociatedServiceRepository;

    public AccountAssociatedServiceService(AccountAssociatedServiceRepository accountAssociatedServiceRepository){
        this.accountAssociatedServiceRepository =  accountAssociatedServiceRepository;
    }

    public RSAccountAssociatedService createAssociatedService(AccountAssociatedService accountAssociatedService){

        accountAssociatedService.setStatus(Status.ACTIVATE.code);
        accountAssociatedService.setStartDate(Utils.currentDate());

        try {
            this.accountAssociatedServiceRepository.save(accountAssociatedService);
        } catch (Exception e) {
            throw new RSRuntimeException(Messages.ACCOUNT_NOT_CREATED, RSCode.INTERNAL_SERVER_ERROR);
        }

        return AccountAssociatedServiceMapper.map(accountAssociatedService);
    }
}
