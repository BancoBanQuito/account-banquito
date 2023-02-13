package com.banquito.account.controller.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RSAccountStatement implements Serializable {

    private String localCodeAccount;

    private String identificationType;

    private String identification;

    private String fullName;

    private LocalDateTime lastCutOffDate;

    private LocalDateTime currentCutOffDate;

    private BigDecimal previousBalance;

    private BigDecimal creditMovements;

    private BigDecimal debitMovements;

    private BigDecimal interest;

    private BigDecimal currentBalance;

    private BigDecimal averageBalance;

    private List<RSAccountStatementTransactions> transactions;
}
