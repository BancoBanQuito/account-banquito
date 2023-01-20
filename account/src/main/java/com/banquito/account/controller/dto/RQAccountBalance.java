package com.banquito.account.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RQAccountBalance implements Serializable {
    private BigDecimal presentBalance;

    private BigDecimal availableBalance;
}
