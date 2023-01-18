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
public class RQBalanceUpdate implements Serializable {
    private BigDecimal presentBalance; 
    private BigDecimal availableBalance; 
}
