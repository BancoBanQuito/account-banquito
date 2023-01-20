package com.banquito.account.request.dto;

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
public class RSTransaction implements Serializable {

    private String codeUniqueTransaction;

    private String movement;

    private String codeLocalAccount;

    private String concept;

    private String executeDate; //dont user LocalDateTime Jackson doesn't support it

    private BigDecimal value;

    private BigDecimal presentBalance;

    private BigDecimal availableBalance;

}
