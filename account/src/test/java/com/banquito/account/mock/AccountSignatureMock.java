package com.banquito.account.mock;

import java.util.Date;

import com.banquito.account.model.AccountSignature;
import com.banquito.account.model.AccountSignaturePK;

public class AccountSignatureMock {
    public AccountSignature createSignature(){
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
}
