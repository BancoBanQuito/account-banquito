package com.banquito.account.model;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode.Include;

@Data
@NoArgsConstructor
@Table(name = "ACCOUNT_STATEMENT_LOG")
@Entity
public class AccountStatementLog {

    @Include
    @EmbeddedId
    private AccountStatementLogPK pk;

    @Column(name = "LAST_CUT_OFF_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastCutOffDate;

    @Column(name = "TYPE", length = 3)
    private String type;

    @Column(name = "INTEREST_RATE", scale = 5, precision = 2)
    private BigDecimal interestRate;

    @Column(name = "ACTUAL_BALANCE", scale = 17, precision = 2)
    private BigDecimal actualBalance;

    @Column(name = "PROM_BALANCE", scale = 17, precision = 2)
    private BigDecimal promBalance;

    public AccountStatementLog(AccountStatementLogPK pk) {
        this.pk = pk;
    }
}
