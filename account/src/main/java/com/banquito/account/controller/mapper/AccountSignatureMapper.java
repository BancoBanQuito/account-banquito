package com.banquito.account.controller.mapper;

import com.banquito.account.controller.dto.RQSignature;
import com.banquito.account.controller.dto.RSSignature;
import com.banquito.account.model.AccountSignature;
import com.banquito.account.model.AccountSignaturePK;

public class AccountSignatureMapper {

    public static AccountSignature map(RQSignature signature) {

        return AccountSignature.builder()
                .pk(AccountSignaturePK.builder()
                        .codeLocalAccount(signature.getCodeLocalAccount())
                        .codeInternationalAccount(signature.getCodeInternationalAccount())
                        .identificationType(signature.getIdentificationType())
                        .identification(signature.getIdentification()).build())
                .startDate(signature.getStartDate())
                .role(signature.getRole()).build();
    }

    public static RSSignature map(AccountSignature signature, String name){

        return RSSignature.builder()
                .identificationType(signature.getPk().getIdentificationType())
                .identification(signature.getPk().getIdentification())
                .name(name)
                .role(signature.getRole())
                .status(signature.getStatus())
                .build();
    }


}
