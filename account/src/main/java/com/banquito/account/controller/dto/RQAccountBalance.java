package com.banquito.account.controller.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RQAccountBalance {
    private BigDecimal presentBalance;

    private BigDecimal availableBalance;
}
