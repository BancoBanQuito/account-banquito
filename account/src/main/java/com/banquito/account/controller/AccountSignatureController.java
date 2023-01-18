package com.banquito.account.controller;

import com.banquito.account.controller.dto.RQSignature;
import com.banquito.account.controller.dto.RQSignatureRoleStatus;
import com.banquito.account.controller.dto.RSSignature;
import com.banquito.account.controller.mapper.AccountSignatureMapper;
import com.banquito.account.exception.RSRuntimeException;
import com.banquito.account.service.AccountSignatureService;
import com.banquito.account.utils.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/account/signature")
public class AccountSignatureController {

    private final AccountSignatureService accountSignatureService;

    public AccountSignatureController(AccountSignatureService accountSignatureService){
        this.accountSignatureService = accountSignatureService;
    }

    @GetMapping(value = "/test")
    public Object test(){
        return accountSignatureService.Test();
    }

    @PostMapping
    public ResponseEntity<RSFormat> createSignature(@RequestBody RQSignature signature) {
        try {
            if(!Utils.hasAllAttributes(signature)){
                throw new RSRuntimeException(Messages.MISSING_PARAMS, RSCode.BAD_REQUEST);
            }
            accountSignatureService.createSignature(AccountSignatureMapper.map(signature));
            return ResponseEntity.status(RSCode.CREATED.code)
                    .body(RSFormat.builder().message("Success").data(Messages.SIGNATURE_CREATED).build());

        } catch (RSRuntimeException e) {
            return ResponseEntity.status(e.getCode())
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());
        }
    }

    @GetMapping(value = "/{identificationType}/{identification}")
    public ResponseEntity<RSFormat> getSignatureList(
            @PathVariable("identificationType") String identificationType,
            @PathVariable("identification") String identification
            ){
        try {
            List<RSSignature> signatures = accountSignatureService.findSignatures(identificationType, identification);
            return ResponseEntity.status(RSCode.CREATED.code)
                    .body(RSFormat.builder().message("Success").data(signatures).build());

        } catch (RSRuntimeException e) {
            return ResponseEntity.status(e.getCode())
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());
        }
    }

    @PutMapping(value = "/{identificationType}/{identification}/{codeLocalAccount}/{codeInternationalAccount}")
    public ResponseEntity<RSFormat> updateSignatureStatus(
            @PathVariable("identificationType") String identificationType,
            @PathVariable("identification") String identification,
            @PathVariable("codeLocalAccount") String codeLocalAccount,
            @PathVariable("codeInternationalAccount") String codeInternationalAccount,
            @RequestBody RQSignatureRoleStatus signatureRoleStatus) {
        try {

            SignatureUpdate signature = SignatureUpdate.builder()
                    .identificationType(identificationType)
                    .identification(identification)
                    .codeLocalAccount(codeLocalAccount)
                    .codeInternationalAccount(codeInternationalAccount)
                    .role(signatureRoleStatus.getRole())
                    .status(signatureRoleStatus.getStatus())
                    .build();

            if(!Utils.hasAllAttributes(signature)){
                throw new RSRuntimeException(Messages.MISSING_PARAMS, RSCode.BAD_REQUEST);
            }

            accountSignatureService.updateSignatureRoleStatus(signature);
            return ResponseEntity.status(RSCode.CREATED.code)
                    .body(RSFormat.builder().message("Success").data(Messages.SIGNATURE_UPDATED).build());

        } catch (RSRuntimeException e) {
            return ResponseEntity.status(e.getCode())
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body(RSFormat.builder().message("Failure").data(e.getMessage()).build());
        }
    }
}
