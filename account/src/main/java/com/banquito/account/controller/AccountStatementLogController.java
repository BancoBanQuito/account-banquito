package com.banquito.account.controller;

import com.banquito.account.utils.Messages;
import com.banquito.account.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banquito.account.utils.RSCode;
import com.banquito.account.utils.RSFormat;
import com.banquito.account.controller.dto.RSAccountStatement;
import com.banquito.account.exception.RSRuntimeException;
import com.banquito.account.service.AccountStatementLogService;

@RestController
@RequestMapping(value = "/api/account/statement")
public class AccountStatementLogController {

    private final AccountStatementLogService accountStatementLogService;

    public AccountStatementLogController(AccountStatementLogService accountStatementLogService) {
        this.accountStatementLogService = accountStatementLogService;
    }

    @GetMapping("/{codeLocalAccount}/{codeInternationalAccount}")
    public ResponseEntity<RSFormat> findAccountStatement(
            @PathVariable("codeLocalAccount") String codeLocalAccount,
            @PathVariable("codeInternationalAccount") String codeInternationalAccount) {
        try {
            if (Utils.isNullEmpty(codeLocalAccount) || Utils.isNullEmpty(codeInternationalAccount)) {
                return ResponseEntity.status(RSCode.BAD_REQUEST.code)
                        .body(RSFormat.builder().message("Failure").data(Messages.MISSING_PARAMS).build());
            }

            RSAccountStatement responseAccountStatement = this.accountStatementLogService
                    .findAccountStatement(codeLocalAccount, codeInternationalAccount);

            return ResponseEntity.status(RSCode.SUCCESS.code)
                    .body(RSFormat.builder().message("Success").data(responseAccountStatement).build());
        } catch (RSRuntimeException e) {
            return ResponseEntity.status(e.getCode())
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());
        } catch (Exception e) {
            return ResponseEntity.status(RSCode.INTERNAL_SERVER_ERROR.code)
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());
        }
    }

}
