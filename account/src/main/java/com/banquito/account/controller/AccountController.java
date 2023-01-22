package com.banquito.account.controller;

import com.banquito.account.controller.dto.*;
import com.banquito.account.request.TransactionRequest;
import com.banquito.account.request.dto.RQInterest;
import com.banquito.account.request.dto.RQTransaction;
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

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    //This method is only used for development purposes
    @GetMapping(value = "/test")
    public Object test(){

        /*return TransactionRequest.createInterest(
                RQInterest.builder()
                        .codeLocalAccount("dfd4f80f8f90f1136512")
                        .codeInternationalAccount("0b6edacd6a13797a079335ca502335a3ad")
                        .ear(BigDecimal.valueOf(5.75))
                        .availableBalance(BigDecimal.valueOf(40.00))
                        .build());*/

        /*return TransactionRequest.getInterestBetweenDates("dfd4f80f8f90f1136512",
                LocalDateTime.parse("2023-01-10T22:47:02.6401231"),
                LocalDateTime.parse("2023-01-22T22:47:02.6401231"));*/

        /*return TransactionRequest.createTransaction(
                RQTransaction.builder()
                        .movement("NOTA CREDITO")
                        .type("INTERES")
                        .codeLocalAccount("dfd4f80f8f90f1136512")
                        .codeInternationalAccount("0b6edacd6a13797a079335ca502335a3ad")
                        .concept("Calculo interes")
                        .value(BigDecimal.valueOf(0.01))
                        .build()
        );*/

         /*return TransactionRequest.getTransactionsBetweenDates("dfd4f80f8f90f1136512",
                LocalDateTime.parse("2023-01-10T22:47:02.6401231"),
                LocalDateTime.parse("2023-01-22T22:47:02.6401231"));*/

        /*return TransactionRequest.getInvestmentInterest("dfd4f80f8f90f1136512",
                91,
                BigDecimal.valueOf(20000),
                BigDecimal.valueOf(2.52));*/

        return accountService.test();
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