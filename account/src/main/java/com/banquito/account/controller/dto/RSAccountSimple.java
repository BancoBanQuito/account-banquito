package com.banquito.account.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RSAccountSimple {

    private String codeLocalAccount;

    private String codeInternationalAccount;

    private String status;

    private String product;
}
