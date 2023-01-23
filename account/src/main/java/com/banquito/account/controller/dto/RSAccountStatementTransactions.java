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
public class RSAccountStatementTransactions implements Serializable {

    private String date;

    private String movement;

    private String concept;

    private BigDecimal amount;

    private BigDecimal balance;
}
