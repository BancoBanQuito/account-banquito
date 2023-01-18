package com.banquito.account.controller.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
public class RQCreateAccount implements Serializable {

    private String identification;

    private String identificationType;

    private String codeBranch;

    private String entityBankCode;

    private String internationalBankCode;

    private String codeProduct;

    private String codeProductType;
}
