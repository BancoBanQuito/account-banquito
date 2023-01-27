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
    public ResponseEntity<RSFormat<RSCreateAccount>> createAccount(@RequestBody RQCreateAccount account) {
        try {

            if (!Utils.hasAllAttributes(account)) {
                return ResponseEntity.status(RSCode.BAD_REQUEST.code).build();
            }

            Account savedAccount = accountService.createAccount(AccountMapper.map(account), account.getIdentification(), account.getIdentificationType());
            RSCreateAccount responseAccount = AccountMapper.map(savedAccount);
            return ResponseEntity.status(RSCode.CREATED.code)
                    .body(RSFormat.<RSCreateAccount>builder().message("Success").data(responseAccount).build());

        } catch (RSRuntimeException e) {
            return ResponseEntity.status(e.getCode()).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping(value = "/id/{identificationType}/{identification}")
    public ResponseEntity<RSFormat<List<RSAccount>>> getConsolidateAccounts(
            @PathVariable("identificationType") String identificationType,
            @PathVariable("identification") String identification) {
        try {

            if (Utils.isNullEmpty(identificationType) || Utils.isNullEmpty(identification)) {
                return ResponseEntity.status(RSCode.BAD_REQUEST.code).build();
            }

            List<RSAccount> rsAccounts = this.accountService.findAllAccountsByClient(identificationType, identification);
            return ResponseEntity.status(RSCode.SUCCESS.code).
                    body(RSFormat.<List<RSAccount>>builder().message("Success").data(rsAccounts).build());

        } catch (RSRuntimeException e) {
            return ResponseEntity.status(e.getCode()).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping(value = "/code/{codeLocalAccount}")
    public ResponseEntity<RSFormat<RSAccount>> getAccountByCode(@PathVariable("codeLocalAccount") String codeLocalAccount) {
        try {

            if (Utils.isNullEmpty(codeLocalAccount)) {
                return ResponseEntity.status(RSCode.BAD_REQUEST.code).build();
            }

            Account account = accountService.findAccountByCode(codeLocalAccount);

            return ResponseEntity.status(RSCode.SUCCESS.code).
                    body(RSFormat.<RSAccount>builder().message("Success").data(AccountMapper.mapAccount(account)).build());

        } catch (RSRuntimeException e) {
            return ResponseEntity.status(e.getCode()).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping(value = "/code/{codeLocalAccount}/type")
    public ResponseEntity<RSFormat<RSProductTypeAndClientName>> getAccountProductTypeAndClientName(@PathVariable("codeLocalAccount") String codeLocalAccount) {
        try {

            if (Utils.isNullEmpty(codeLocalAccount)) {
                return ResponseEntity.status(RSCode.BAD_REQUEST.code).build();
            }

            RSProductTypeAndClientName response = accountService.getAccountProductTypeAndClientName(codeLocalAccount);

            return ResponseEntity.status(RSCode.SUCCESS.code).
                    body(RSFormat.<RSProductTypeAndClientName>builder().message("Success").data(response).build());

        } catch (RSRuntimeException e) {
            return ResponseEntity.status(e.getCode()).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping(value = "/code/{codeLocalAccount}/status")
    public ResponseEntity<RSFormat<String>> updateAccountStatus(
            @PathVariable("codeLocalAccount") String codeLocalAccount,
            @RequestBody RQAccountStatus status) {
        try {
            if (!Utils.hasAllAttributes(status) || Utils.isNullEmpty(codeLocalAccount)) {
                return ResponseEntity.status(RSCode.BAD_REQUEST.code).build();
            }

            accountService.updateAccountStatus(codeLocalAccount,status.getStatus());
            return ResponseEntity.status(RSCode.CREATED.code)
                    .body(RSFormat.<String>builder().message("Success").data(Messages.SIGNATURE_UPDATED).build());
        } catch (RSRuntimeException e) {
            return ResponseEntity.status(e.getCode()).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }


    @PutMapping(value = "/code/{codeLocalAccount}/balance")
    public ResponseEntity<RSFormat<String>> updateAccountBalance(
            @PathVariable("codeLocalAccount") String codeLocalAccount,
            @RequestBody RQAccountBalance balance) {
        try {
            if (!Utils.hasAllAttributes(balance) || Utils.isNullEmpty(codeLocalAccount)) {
                return ResponseEntity.status(RSCode.BAD_REQUEST.code).build();
            }
            accountService.updateAccountBalance(
                    codeLocalAccount,
                    balance.getPresentBalance(),
                    balance.getAvailableBalance());
            return ResponseEntity.status(RSCode.CREATED.code)
                    .body(RSFormat.<String>builder().message("Success").data(Messages.ACCOUNT_UPDATED).build());

        } catch (RSRuntimeException e) {
            return ResponseEntity.status(e.getCode()).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

}