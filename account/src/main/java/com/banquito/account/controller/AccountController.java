package com.banquito.account.controller;

import com.banquito.account.controller.dto.*;
import com.banquito.account.utils.Messages;
import com.banquito.account.utils.RSCode;
import com.banquito.account.utils.RSFormat;
import com.banquito.account.controller.mapper.AccountMapper;
import com.banquito.account.exception.RSRuntimeException;
import com.banquito.account.model.Account;
import com.banquito.account.service.AccountService;
import com.banquito.account.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<RSFormat> createAccount(@RequestBody RQCreateAccount account) {
        try {

            if (!Utils.hasAllAttributes(account)) {
                return ResponseEntity.status(RSCode.BAD_REQUEST.code)
                        .body(RSFormat.builder().message("Failure").data(Messages.MISSING_PARAMS).build());
            }

            Account savedAccount = accountService.createAccount(AccountMapper.map(account), account.getIdentification(), account.getIdentificationType());
            RSCreateAccount responseAccount = AccountMapper.map(savedAccount);
            return ResponseEntity.status(RSCode.CREATED.code)
                    .body(RSFormat.builder().message("Success").data(responseAccount).build());

        } catch (RSRuntimeException e) {
            return ResponseEntity.status(e.getCode())
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());

        } catch (Exception e) {
            return ResponseEntity.status(RSCode.INTERNAL_SERVER_ERROR.code)
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());
        }
    }

    @GetMapping(value = "/id/{identificationType}/{identification}")
    public ResponseEntity<RSFormat> getConsolidateAccounts(
            @PathVariable("identificationType") String identificationType,
            @PathVariable("identification") String identification) {
        try {

            if (Utils.isNullEmpty(identificationType) || Utils.isNullEmpty(identification)) {
                return ResponseEntity.status(RSCode.BAD_REQUEST.code)
                        .body(RSFormat.builder().message("Failure").data(Messages.MISSING_PARAMS).build());
            }

            List<RSAccount> rsAccounts = this.accountService.findAllAccountsByClient(identificationType, identification);
            return ResponseEntity.status(RSCode.SUCCESS.code).
                    body(RSFormat.builder().message("Success").data(rsAccounts).build());

        } catch (RSRuntimeException e) {
            return ResponseEntity.status(e.getCode())
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());

        } catch (Exception e) {
            return ResponseEntity.status(RSCode.INTERNAL_SERVER_ERROR.code)
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());
        }
    }

    @GetMapping(value = "/code/{codeLocalAccount}/{codeInternationalAccount}")
    public ResponseEntity<RSFormat> getAccountByCode(
            @PathVariable("codeLocalAccount") String codeLocalAccount,
            @PathVariable("codeInternationalAccount") String codeInternationalAccount) {
        try {

            if (Utils.isNullEmpty(codeLocalAccount) || Utils.isNullEmpty(codeInternationalAccount)) {
                return ResponseEntity.status(RSCode.BAD_REQUEST.code)
                        .body(RSFormat.builder().message("Failure").data(Messages.MISSING_PARAMS).build());
            }

            Account account = accountService.findAccountByCode(codeLocalAccount, codeInternationalAccount);

            return ResponseEntity.status(RSCode.SUCCESS.code).
                    body(RSFormat.builder().message("Success").data(AccountMapper.mapAccount(account)).build());

        } catch (RSRuntimeException e) {
            return ResponseEntity.status(e.getCode())
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());
        }
    }

    @PutMapping(value = "/code/{codeLocalAccount}/{codeInternationalAccount}/status")
    public ResponseEntity<RSFormat> updateAccountStatus(
            @PathVariable("codeLocalAccount") String codeLocalAccount,
            @PathVariable("codeInternationalAccount") String codeInternationalAccount,
            @RequestBody RQAccountStatus status) {
        try {
            if (!Utils.hasAllAttributes(status) || Utils.isNullEmpty(codeLocalAccount) || Utils.isNullEmpty(codeInternationalAccount)) {
                return ResponseEntity.status(RSCode.BAD_REQUEST.code)
                        .body(RSFormat.builder().message("Failure").data(Messages.MISSING_PARAMS).build());
            }

            accountService.updateAccountStatus(codeLocalAccount, codeInternationalAccount, status.getStatus());
            return ResponseEntity.status(RSCode.CREATED.code)
                    .body(RSFormat.builder().message("Success").data(Messages.SIGNATURE_UPDATED).build());

        } catch (RSRuntimeException e) {
            return ResponseEntity.status(e.getCode())
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());

        } catch (Exception e) {
            return ResponseEntity.status(RSCode.INTERNAL_SERVER_ERROR.code)
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());
        }
    }


    @PutMapping(value = "/code/{codeLocalAccount}/{codeInternationalAccount}/balance")
    public ResponseEntity<RSFormat> updateAccountBalance(
            @PathVariable("codeLocalAccount") String codeLocalAccount,
            @PathVariable("codeInternationalAccount") String codeInternationalAccount,
            @RequestBody RQAccountBalance balance) {
        try {
            if (!Utils.hasAllAttributes(balance) || Utils.isNullEmpty(codeLocalAccount) || Utils.isNullEmpty(codeInternationalAccount)) {
                return ResponseEntity.status(RSCode.BAD_REQUEST.code)
                        .body(RSFormat.builder().message("Failure").data(Messages.MISSING_PARAMS).build());
            }
            accountService.updateAccountBalance(
                    codeLocalAccount,
                    codeInternationalAccount,
                    balance.getPresentBalance(),
                    balance.getAvailableBalance());
            return ResponseEntity.status(RSCode.CREATED.code)
                    .body(RSFormat.builder().message("Success").data(Messages.ACCOUNT_UPDATED).build());

        } catch (RSRuntimeException e) {
            return ResponseEntity.status(e.getCode())
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());

        } catch (Exception e) {
            return ResponseEntity.status(RSCode.INTERNAL_SERVER_ERROR.code)
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());
        }
    }

}