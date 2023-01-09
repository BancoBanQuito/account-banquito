package com.banquito.account.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.banquito.account.model.AccountSignature;
import com.banquito.account.service.AccountSignatureService;

@RestController
@RequestMapping(value = "/api/account-signature")
public class AccountSignatureController {
    private final AccountSignatureService accountSignatureService;

    public AccountSignatureController(AccountSignatureService accountSignatureService) {
        this.accountSignatureService = accountSignatureService;
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<Object> createAccountSignature(
            @RequestBody AccountSignature accountSignature) {
        return ResponseEntity.status(201).body("Object created");
    }

    @RequestMapping(value = "/local/{codeLocalAccount}/{identification}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteAccountSignature(
            @PathVariable("codeLocalAccount") String codeLocalAccount,
            @PathVariable("identification") String identification) {
        return ResponseEntity.status(201).body("Object deleted");
    }

    @RequestMapping(value = "/localAcc/{local}/internationalAcc/{international}", method = RequestMethod.GET)
    //must return a list dto AccountSignatureIdentificationDatesDto
    public ResponseEntity<Object> findByAccount(
            @PathVariable("local") String local, @PathVariable("international") String international) {
        return ResponseEntity.status(201).body("List returned");
    }

}
