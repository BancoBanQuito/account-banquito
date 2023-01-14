package com.banquito.account.controller.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RSAccountStatement implements Serializable {
    private String fullname;
    private String accountCode;
    private String clientIdentification;
    private Date lastCutOffDate;
    private Date actualCutOffDate;
    private BigDecimal lastBalance;
    private BigDecimal interestRate;
    private BigDecimal presentBalance;
    private BigDecimal promBalance;
    private List<Transaction> transactions;

    @Data
    public class Transaction implements Serializable {
        private String type;
        private String description;
        private String movement;
        private BigDecimal amount;
        private BigDecimal balance;
    }
}
