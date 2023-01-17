package com.banquito.account.controller;

import com.banquito.account.utils.RSCode;
import com.banquito.account.utils.ResponseFormat;
import com.banquito.account.controller.dto.RQCreateAccount;
import com.banquito.account.controller.dto.RSAccount;
import com.banquito.account.controller.dto.RSCreateAccount;
import com.banquito.account.controller.mapper.AccountMapper;
import com.banquito.account.exception.RSRuntimeException;
import com.banquito.account.model.Account;
import com.banquito.account.service.AccountService;
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
    public ResponseEntity<ResponseFormat> createAccount(@RequestBody RQCreateAccount account) {
        try {
            Account savedAccount = accountService.createAccount(AccountMapper.map(account), account.getIdentification(), account.getIdentificationType());
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

    @GetMapping("/{identificationType}/{identification}")
    public ResponseEntity<ResponseFormat> getConsolidateAccounts(
        @PathVariable("identificationType") String identificationType,
        @PathVariable("identification") String identification) {
        try {
            List<RSAccount> rsAccounts = this.accountService.findAllAccountsByClient(identificationType, identification);
            return ResponseEntity.status(RSCode.SUCCESS.code).
                    body(ResponseFormat.builder().message("Success").data(rsAccounts).build());
        } catch (RSRuntimeException e) {
            return ResponseEntity.status(e.getCode())
                        .body(ResponseFormat.builder().message("Failure").data(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.status(500)
                        .body(ResponseFormat.builder().message("Failure").data(e.getMessage()).build());
        }
    }
}