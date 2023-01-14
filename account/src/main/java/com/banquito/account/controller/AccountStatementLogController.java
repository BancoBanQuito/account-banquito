package com.banquito.account.controller;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banquito.account.config.RSCode;
import com.banquito.account.config.ResponseFormat;
import com.banquito.account.controller.dto.RSAccountStatement;
import com.banquito.account.errors.RSRuntimeException;
import com.banquito.account.service.AccountStatementLogService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(value = "/api/account/statement")
public class AccountStatementLogController {

    private final AccountStatementLogService accountStatementLogService;

    public AccountStatementLogController(AccountStatementLogService accountStatementLogService) {
        this.accountStatementLogService = accountStatementLogService;
    }

    @GetMapping("/{account-code}")
    public ResponseEntity<ResponseFormat> findAccountStatement(@PathVariable("account-code") String accountCode) {
        try {
            RSAccountStatement rsAccountStatement = this.accountStatementLogService.findAccountStatement(accountCode);
            return ResponseEntity.status(RSCode.SUCCESS.code)
                    .body(ResponseFormat.builder().message("Success").data(rsAccountStatement).build());
        } catch (RSRuntimeException e) {
            return ResponseEntity.status(e.getCode())
                    .body(ResponseFormat.builder().message("Failure").data(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(ResponseFormat.builder().message("Failure").data(e.getMessage()).build());
        }
    }

}
