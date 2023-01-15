package com.banquito.account.controller;

import com.banquito.account.config.RSCode;
import com.banquito.account.config.ResponseFormat;
import com.banquito.account.controller.dto.RQCreateAccount;
import com.banquito.account.controller.dto.RQCreateAccountSignature;
import com.banquito.account.controller.dto.RSCreateAccount;
import com.banquito.account.controller.dto.RSCreateAccountSignature;
import com.banquito.account.controller.mapper.AccountMapper;
import com.banquito.account.controller.mapper.AccountSignatureMapper;
import com.banquito.account.errors.RSRuntimeException;
import com.banquito.account.model.Account;
import com.banquito.account.model.AccountSignature;
import com.banquito.account.service.AccountService;
import com.banquito.account.service.AccountSignatureService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping(value = "/api/account")
public class AccountController {
    private final AccountService accountService;
    private final AccountSignatureService accountSignatureService;

    public AccountController(AccountService accountService, AccountSignatureService accountSignatureService) {
        this.accountService = accountService;
        this.accountSignatureService = accountSignatureService;
    }

    @PostMapping
    public ResponseEntity<ResponseFormat> createAccount(
            @RequestBody RQCreateAccount account) {
        try {
            Account savedAccount = accountService.createAccount(AccountMapper.map(account), account.getIdentification(),
                    account.getIdentificationType());
            RSCreateAccount responseAccount = AccountMapper.map(savedAccount);
            return ResponseEntity.status(RSCode.CREATED.code)
                    .body(ResponseFormat.builder().message("Success").data(responseAccount).build());
        } catch (RSRuntimeException e) {
            return ResponseEntity.status(e.getCode())
                    .body(ResponseFormat.builder().message("Failure").data(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ResponseFormat.builder().message("Failure").data(e.getMessage()).build());
        }
    }

    @RequestMapping(value = "/signature/{identificationType}/{identification}/{codeLocalAccount}/{codeInternationalAccount}", method = RequestMethod.POST)
    public ResponseEntity<ResponseFormat> createAccountSignature(
            @PathVariable("identificationType") String identificationType,
            @PathVariable("identification") String identification,
            @PathVariable("codeLocalAccount") String codeLocalAccount,
            @PathVariable("codeInternationalAccount") String codeInternationalAccount,
            @RequestBody RQCreateAccountSignature rqCreateAccountSignature) {
        try {
            // estos datos deben ser retornados de API Cliente
            String signature = "abc";
            String name = "Madely Betaancourt";
            AccountSignature savedAccountSignature = accountSignatureService.createAccountSignature(
                    AccountSignatureMapper.mapCreation(rqCreateAccountSignature),
                    identification, identificationType,
                    codeLocalAccount,
                    codeInternationalAccount, signature);

            RSCreateAccountSignature responseAccountSignature = AccountSignatureMapper
                    .mapCreation(savedAccountSignature, name, signature);
            return ResponseEntity.status(RSCode.CREATED.code)
                    .body(ResponseFormat.builder().message("Success").data(responseAccountSignature).build());
        } catch (RSRuntimeException e) {
            return ResponseEntity.status(e.getCode())
                    .body(ResponseFormat.builder().message("Failure").data(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ResponseFormat.builder().message("Failure").data(e.getMessage()).build());
        }
    }

    @RequestMapping(value = "accountCode/{account-code}/signature", method = RequestMethod.GET)
    // must return a list dto AccountSignatureIdentificationDatesDto
    public ResponseEntity<Object> findSignatureByAccount(
            @PathVariable("account-code") String accountCode) {
        return ResponseEntity.status(200).body("List returned");
    }

    @RequestMapping(value = "/{account-code}/statement", method = RequestMethod.GET)
    public ResponseEntity<Object> getAccountStatement(
            @PathVariable("account-code") String accountCode) {
        return ResponseEntity.status(200).body("Account Statement");
    }

    @RequestMapping(value = "/{account-code}", method = RequestMethod.GET)
    public ResponseEntity<Object> suspendAccount(
            @PathVariable("account-code") String accountCode) {
        return ResponseEntity.status(200).body("Suspend Account");
    }

    @RequestMapping(value = "position/{identification-type}/{identification}", method = RequestMethod.GET)
    public ResponseEntity<Object> getConsolidatedPosition(
            @PathVariable("identification-type") String identificationType,
            @PathVariable("identification") String identification) {
        return ResponseEntity.status(200).body("Consolidate Position");
    }

    @RequestMapping(value = "list/{identification-type}/{identification}", method = RequestMethod.GET)
    public ResponseEntity<Object> getClientAccounts(
            @PathVariable("identification-type") String identificationType,
            @PathVariable("identification") String identification) {
        return ResponseEntity.status(200).body("Get Client Accounts");
    }

    @RequestMapping(value = "/{account-code}", method = RequestMethod.PUT)
    public ResponseEntity<Object> updateAccountBalance(
            @PathVariable("account-code") String accountCode,
            @RequestBody BigDecimal balance) {
        return ResponseEntity.status(200).body("Update Account Balance");
    }

    @RequestMapping(value = "/{account-code}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> inactiveAccount(
            @PathVariable("account-code") String accountCode) {
        return ResponseEntity.status(200).body("Change active account state");
    }

    @RequestMapping(value = "accountCode/{account-code}/signature", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAccountSignature(
            @PathVariable("account-code") String accountCode) {
        return ResponseEntity.status(201).body("Object deleted");
    }
}