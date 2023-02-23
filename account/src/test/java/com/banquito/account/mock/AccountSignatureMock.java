package com.banquito.account.mock;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.banquito.account.controller.dto.RSSignature;
import com.banquito.account.model.Account;
import com.banquito.account.model.AccountPK;
import com.banquito.account.model.AccountSignature;
import com.banquito.account.model.AccountSignaturePK;
import com.banquito.account.request.dto.RSClientSignature;

public class AccountSignatureMock {
    public static AccountSignature createSignature(){
        return AccountSignature.builder()
                .pk(AccountSignaturePK.builder()
                        .codeLocalAccount("12345678901234567890")
                        .codeInternationalAccount("1234567890123456789012345678901234")
                        .identificationType("CED")
                        .identification("12345678901234567890")
                        .build())
                .signatureReference("abc")
                .role("Primario")
                .status("Activo")
                .createDate(new Date())
                .startDate(new Date())
                .endDate(new Date())
                .build();
    }
    public static List <AccountSignature> listCreateSignature(){
        return List.of(createSignature(), createSignature(), createSignature());
    }

    public static RSSignature createRSSSignature(){
        return RSSignature.builder()
                .identificationType("CED")
                .identification("12345678901234567890")
                .name("Juan Perez")
                .role("Primario")
                .status("Activo")
                .signature("abc")
                .build();
    }

    public static List <RSSignature> listCreateRSSignature(){
        return List.of(createRSSSignature(), createRSSSignature(), createRSSSignature());
    }

    public static RSClientSignature createRSClientSignature(){
        return RSClientSignature.builder()
                .typeIndentification("CED")
                .identification("12345678901234567890")
                .name("Juan")
                .lastName("Perez")
                .signature("abc")
                .build();
    }

    public static Account createAccount(){
        return Account.builder()
                .pk(AccountPK.builder()
                        .codeLocalAccount("12345678901234567890")
                        .codeInternationalAccount("1234567890123456789012345678901234")
                        .build())
                .codeProduct("ABC123")
                .codeProductType("123abc")
                .codeBranch("123")
                .entityBankCode("DFF")
                .internationalBankCode("001")
                .status("Activo")
                .createDate(new Date())
                .lastUpdateDate(new Date())
                .closeDate(new Date())
                .presentBalance(new BigDecimal(200))
                .availableBalance(new BigDecimal(200))
                .build();
    }
}
