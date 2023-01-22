package com.banquito.account.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RSInterest {

    private String codeLocalAccount;

    private String codeInternationalAccount;

    private String codeUniqueInterest;

    private BigDecimal ear;

    private BigDecimal availableBalance;

    private BigDecimal value;

    private String executeDate; //dont user LocalDateTime Jackson doesn't support
}
