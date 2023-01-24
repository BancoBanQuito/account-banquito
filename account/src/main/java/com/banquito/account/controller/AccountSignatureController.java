package com.banquito.account.controller;

import com.banquito.account.controller.dto.RQSignature;
import com.banquito.account.controller.dto.RQSignatureRoleStatus;
import com.banquito.account.controller.dto.RSSignature;
import com.banquito.account.controller.mapper.AccountSignatureMapper;
import com.banquito.account.exception.RSRuntimeException;
import com.banquito.account.request.ClientRequest;
import com.banquito.account.service.AccountSignatureService;
import com.banquito.account.utils.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/account/signature")
public class AccountSignatureController {

    private final AccountSignatureService accountSignatureService;

    public AccountSignatureController(AccountSignatureService accountSignatureService){
        this.accountSignatureService = accountSignatureService;
    }

    @PostMapping
    public ResponseEntity<RSFormat> createSignature(@RequestBody RQSignature signature) {
        try {
            if(!Utils.hasAllAttributes(signature)){
                return ResponseEntity.status(RSCode.BAD_REQUEST.code)
                        .body(RSFormat.builder().message("Failure").data(Messages.MISSING_PARAMS).build());
            }
            accountSignatureService.createSignature(AccountSignatureMapper.map(signature));
            return ResponseEntity.status(RSCode.CREATED.code)
                    .body(RSFormat.builder().message("Success").data(Messages.SIGNATURE_CREATED).build());

        } catch (RSRuntimeException e) {
            return ResponseEntity.status(e.getCode())
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());

        } catch (Exception e) {
            return ResponseEntity.status(RSCode.INTERNAL_SERVER_ERROR.code)
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());
        }
    }

    @GetMapping(value = "/id/{identificationType}/{identification}")
    public ResponseEntity<RSFormat> getSignatureListById(
            @PathVariable("identificationType") String identificationType,
            @PathVariable("identification") String identification
            ){
        try {
            if(Utils.isNullEmpty(identificationType) || Utils.isNullEmpty(identification)){
                return ResponseEntity.status(RSCode.BAD_REQUEST.code)
                        .body(RSFormat.builder().message("Failure").data(Messages.MISSING_PARAMS).build());
            }
            List<RSSignature> signatures = accountSignatureService.findSignaturesById(identificationType, identification);
            return ResponseEntity.status(RSCode.CREATED.code)
                    .body(RSFormat.builder().message("Success").data(signatures).build());

        } catch (RSRuntimeException e) {
            return ResponseEntity.status(e.getCode())
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());

        } catch (Exception e) {
            return ResponseEntity.status(RSCode.INTERNAL_SERVER_ERROR.code)
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());
        }
    }
    @GetMapping(value = "/code/{codeLocalAccount}")
    public ResponseEntity<RSFormat> getSignatureListByCode(@PathVariable("codeLocalAccount") String codeLocalAccount){
        try {
            if(Utils.isNullEmpty(codeLocalAccount)){
                return ResponseEntity.status(RSCode.BAD_REQUEST.code)
                        .body(RSFormat.builder().message("Failure").data(Messages.MISSING_PARAMS).build());
            }
            List<RSSignature> signatures = accountSignatureService.findSignaturesByCode(codeLocalAccount);
            return ResponseEntity.status(RSCode.CREATED.code)
                    .body(RSFormat.builder().message("Success").data(signatures).build());

        } catch (RSRuntimeException e) {
            return ResponseEntity.status(e.getCode())
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());

        } catch (Exception e) {
            return ResponseEntity.status(RSCode.INTERNAL_SERVER_ERROR.code)
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());
        }
    }

    @PutMapping(value = "/{identificationType}/{identification}/{codeLocalAccount}")
    public ResponseEntity<RSFormat> updateSignatureStatus(
            @PathVariable("identificationType") String identificationType,
            @PathVariable("identification") String identification,
            @PathVariable("codeLocalAccount") String codeLocalAccount,
            @RequestBody RQSignatureRoleStatus signatureRoleStatus) {
        try {

            if(!Utils.hasAllAttributes(signatureRoleStatus)
                    || Utils.isNullEmpty(identificationType)
                    || Utils.isNullEmpty(identification)
                    || Utils.isNullEmpty(codeLocalAccount)){
                return ResponseEntity.status(RSCode.BAD_REQUEST.code)
                        .body(RSFormat.builder().message("Failure").data(Messages.MISSING_PARAMS).build());
            }

            accountSignatureService.updateSignatureRoleStatus(
                    identificationType,
                    identification,
                    codeLocalAccount,
                    signatureRoleStatus.getRole(),
                    signatureRoleStatus.getStatus()
            );
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
}
