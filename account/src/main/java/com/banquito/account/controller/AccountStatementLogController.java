package com.banquito.account.controller;

import com.banquito.account.controller.dto.RSAccountStatement;
import com.banquito.account.controller.dto.RSAccountStatementList;
import com.banquito.account.exception.RSRuntimeException;
import com.banquito.account.service.AccountStatementLogService;
import com.banquito.account.utils.RSCode;
import com.banquito.account.utils.RSFormat;
import com.banquito.account.utils.Utils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/account/statement")
public class AccountStatementLogController {

    private final AccountStatementLogService accountStatementLogService;

    public AccountStatementLogController(AccountStatementLogService accountStatementLogService) {
        this.accountStatementLogService = accountStatementLogService;
    }

    @GetMapping("/current/{codeLocalAccount}")
    public ResponseEntity<RSFormat<RSAccountStatement>> findCurrentAccountStatement(@PathVariable("codeLocalAccount") String codeLocalAccount) {
        try {
            if (Utils.isNullEmpty(codeLocalAccount)) {
                return ResponseEntity.status(RSCode.BAD_REQUEST.code).build();
            }

            RSAccountStatement response = this.accountStatementLogService
                    .findCurrentAccountStatement(codeLocalAccount);

            return ResponseEntity.status(RSCode.SUCCESS.code)
                    .body(RSFormat.<RSAccountStatement>builder().message("Success").data(response).build());
        } catch (RSRuntimeException e) {
            return ResponseEntity.status(e.getCode()).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/list/{codeLocalAccount}")
    public ResponseEntity<RSFormat<List<RSAccountStatementList>>> findAccountStatementList(@PathVariable("codeLocalAccount") String codeLocalAccount) {
        try {
            if (Utils.isNullEmpty(codeLocalAccount)) {
                return ResponseEntity.status(RSCode.BAD_REQUEST.code).build();
            }

            List<RSAccountStatementList> response = accountStatementLogService.findAccountStatementList(
                    codeLocalAccount);

            return ResponseEntity.status(RSCode.SUCCESS.code)
                    .body(RSFormat.<List<RSAccountStatementList>>builder().message("Success").data(response).build());
        } catch (RSRuntimeException e) {
            return ResponseEntity.status(e.getCode()).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping("/historic/{codeAccountStateLog}")
    public ResponseEntity<RSFormat<RSAccountStatement>> findHistoricAccountStatement(
            @PathVariable("codeAccountStateLog") String codeAccountStateLog) {
        try {
            if (Utils.isNullEmpty(codeAccountStateLog)) {
                return ResponseEntity.status(RSCode.BAD_REQUEST.code).build();
            }

            RSAccountStatement response = this.accountStatementLogService
                    .findHistoricAccountStatement(codeAccountStateLog);

            return ResponseEntity.status(RSCode.SUCCESS.code)
                    .body(RSFormat.<RSAccountStatement>builder().message("Success").data(response).build());
        } catch (RSRuntimeException e) {
            return ResponseEntity.status(e.getCode()).build();
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
