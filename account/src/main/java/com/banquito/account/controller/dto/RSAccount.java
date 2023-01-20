package com.banquito.account.controller.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RSAccount implements Serializable {
    private String codeLocalAccount;

    private String codeInternationalAccount;

    private String status;

    private String product;

    private BigDecimal presentBalance;

    private BigDecimal availableBalance;
}
