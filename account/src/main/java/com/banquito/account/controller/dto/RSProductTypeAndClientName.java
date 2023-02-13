package com.banquito.account.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RSProductTypeAndClientName {

    private String codeLocalAccount;

    private String productType;

    private String product;

    private String identificationType;

    private String identification;

    private String name;
}
