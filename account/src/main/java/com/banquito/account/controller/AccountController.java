package com.banquito.account.controller;

import com.banquito.account.model.Account;
import com.banquito.account.model.AccountSignature;
import com.banquito.account.service.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping(value = "/api/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccount(
            @RequestBody Account account) {
        return ResponseEntity.status(200).body("Object created");
    }

    @RequestMapping(value = "/{account-code}", method = RequestMethod.GET)
    public ResponseEntity<Object> suspendAccount(
            @PathVariable("account-code") String accountCode) {
        return ResponseEntity.status(200).body("Suspend Account");
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

    @RequestMapping(value = "/{account-code}/statement", method = RequestMethod.GET)
    public ResponseEntity<Object> getAccountStatement(
            @PathVariable("account-code") String accountCode) {
        return ResponseEntity.status(200).body("Account Statement");
    }

    @RequestMapping(value = "/{account-code}/signature", method = RequestMethod.GET)
    public ResponseEntity<Object> getAssociatedSignature(
            @PathVariable("account-code") String accountCode) {
        return ResponseEntity.status(200).body("Get Associated Signature");
    }

    @RequestMapping(value = "/{account-code}/signature", method = RequestMethod.POST)
    public ResponseEntity<Object> createAssociatedSignature(
            @PathVariable("account-code") String accountCode,
            @RequestBody AccountSignature signature) {
        return ResponseEntity.status(200).body("Create Associated Signature");
    }

    @RequestMapping(value = "/{account-code}/signature/{signature-code}", method = RequestMethod.PUT)
    public ResponseEntity<Object> suspendAssociatedSignature(
            @PathVariable("account-code") String accountCode,
            @PathVariable("signature-code") String signatureCode) {
        return ResponseEntity.status(200).body("Suspend Associated Signature");
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

    @RequestMapping(value = "/product/service/{account-code}", method = RequestMethod.GET)
    public ResponseEntity<Object> getAssociatedProducts(
            @PathVariable("account-code") String accountCode) {
        return ResponseEntity.status(200).body("Get Associated Products");
    }

    @RequestMapping(value = "/product/service/{account-code}", method = RequestMethod.DELETE)
    public ResponseEntity<Object> inactiveProduct(
            @PathVariable("account-code") String accountCode) {
        return ResponseEntity.status(200).body("Change active product state");
    }
}