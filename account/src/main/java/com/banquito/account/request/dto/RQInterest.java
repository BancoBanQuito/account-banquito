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
public class RQInterest {

    private String codeLocalAccount;

    private String codeInternationalAccount;

    private BigDecimal ear;

    private BigDecimal availableBalance;

}
