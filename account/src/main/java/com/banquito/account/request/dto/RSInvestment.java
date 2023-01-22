package com.banquito.account.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RSInvestment {

    private String codeLocalAccount;

    private BigDecimal rawInterest;

    private BigDecimal retention;

    private BigDecimal netInterest;

}
