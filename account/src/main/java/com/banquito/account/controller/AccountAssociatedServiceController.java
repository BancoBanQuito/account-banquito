package com.banquito.account.controller;

import com.banquito.account.controller.dto.RQAccountAssociatedService;
import com.banquito.account.controller.dto.RQCreateAccount;
import com.banquito.account.controller.dto.RSAccountAssociatedService;
import com.banquito.account.controller.dto.RSCreateAccount;
import com.banquito.account.controller.mapper.AccountAssociatedServiceMapper;
import com.banquito.account.controller.mapper.AccountMapper;
import com.banquito.account.exception.RSRuntimeException;
import com.banquito.account.model.Account;
import com.banquito.account.model.AccountAssociatedService;
import com.banquito.account.service.AccountAssociatedServiceService;
import com.banquito.account.utils.Messages;
import com.banquito.account.utils.RSCode;
import com.banquito.account.utils.RSFormat;
import com.banquito.account.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/account/associated/service")
public class AccountAssociatedServiceController {

    private final AccountAssociatedServiceService accountAssociatedServiceService;

    public AccountAssociatedServiceController(AccountAssociatedServiceService accountAssociatedServiceService){
        this.accountAssociatedServiceService = accountAssociatedServiceService;
    }


    @PostMapping
    public ResponseEntity<RSFormat> createAccount(@RequestBody RQAccountAssociatedService accountAssociatedService) {
        try {

            if (!Utils.hasAllAttributes(accountAssociatedService)) {
                return ResponseEntity.status(RSCode.BAD_REQUEST.code)
                        .body(RSFormat.builder().message("Failure").data(Messages.MISSING_PARAMS).build());
            }

            RSAccountAssociatedService response = accountAssociatedServiceService.createAssociatedService(
                    AccountAssociatedServiceMapper.map(accountAssociatedService));

            return ResponseEntity.status(RSCode.CREATED.code)
                    .body(RSFormat.builder().message("Success").data(response).build());

        } catch (RSRuntimeException e) {
            return ResponseEntity.status(e.getCode())
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());

        } catch (Exception e) {
            return ResponseEntity.status(RSCode.INTERNAL_SERVER_ERROR.code)
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());
        }
    }

}
