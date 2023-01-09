package com.banquito.account.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;

@Data
@Embeddable
public class AccountStatementLogPK implements Serializable {

    @Column(name = "CODE_LOCAL_ACCOUNT", length = 20, nullable = false)
    private String codeLocalAccount;

    @Column(name = "CODE_INTERNTATIONAL_ACCOUNT", length = 34, nullable = false)
    private String codeInternationalAccount;

    @Column(name = "ACTUAL_CUT_OFF_DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date actualCutOffDate;
}
