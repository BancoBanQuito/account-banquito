package com.banquito.account.controller.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RQCreateAccount implements Serializable {

    private String identification;

    private String identificationType;

    private String codeBranch;

    private String entityBankCode;

    private String internationalBankCode;

    private String codeProduct;

    private String codeProductType;
}
