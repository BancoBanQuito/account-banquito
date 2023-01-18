package com.banquito.account.model;

import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode.Include;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "account_statement_log")
public class AccountStatementLog {

    @EmbeddedId
    @Include
    private AccountStatementLogPK pk;

    @Column(name = "current_cut_off_date")
    @Temporal(TemporalType.DATE)
    private Date currentCutOffDate;

    @Column(name = "last_cut_off_date")
    @Temporal(TemporalType.DATE)
    private Date lastCutOffDate;

    @Column(name = "previous_balance", scale = 17, precision = 2)
    private BigDecimal previousBalance;

    @Column(name = "credit_movements", scale = 17, precision = 2)
    private BigDecimal creditMovements;

    @Column(name = "debit_movements", scale = 17, precision = 2)
    private BigDecimal debitMovements;

    @Column(name = "interest", scale = 17, precision = 2)
    private BigDecimal interest;

    @Column(name = "current_balance", scale = 17, precision = 2)
    private BigDecimal currentBalance;

    @Column(name = "average_cash_balance", scale = 17, precision = 2)
    private BigDecimal averageCashBalance;

    @Version
	private Long version;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "code_local_account", referencedColumnName = "code_local_account", insertable = false, updatable = false),
            @JoinColumn(name = "code_international_account", referencedColumnName = "code_international_account", insertable = false, updatable = false),
    })
    private Account account;
}
