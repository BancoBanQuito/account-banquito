package com.banquito.account.controller.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RSAccount implements Serializable {
    private String codeLocalAccount;

    private String codeInternationalAccount;

    private String status;

    private String product;

    private BigDecimal presentBalance;

    private BigDecimal availableBalance;
}
