package com.banquito.account.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RSAccountStatementList {

    public String code;

    public Date currentCutOffDate;

    public BigDecimal credit;

    public BigDecimal debit;

    public BigDecimal interest;

    public BigDecimal balance;
}
