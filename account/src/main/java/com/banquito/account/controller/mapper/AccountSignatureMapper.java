package com.banquito.account.controller.mapper;

import com.banquito.account.controller.dto.RQCreateAccountSignature;
import com.banquito.account.controller.dto.RSCreateAccountSignature;
import com.banquito.account.model.AccountSignature;

public class AccountSignatureMapper {

    //david no puso los datos que obtiene de otras apis
    public static AccountSignature mapCreation(RQCreateAccountSignature rqAccountSignature) {
        return AccountSignature.builder()
        .role(rqAccountSignature.getRole())
        .build();
    }

    public static RSCreateAccountSignature mapCreation (AccountSignature accountSignature, String name, String signature){
        return RSCreateAccountSignature.builder()
        .identification(accountSignature.getPk().getIdentification())
        .identificationtype(accountSignature.getPk().getIdentificationtype())
        .role(accountSignature.getRole())
        .status(accountSignature.getStatus())
        .name(name)
        .signature(signature)
        .build();
    }

}
