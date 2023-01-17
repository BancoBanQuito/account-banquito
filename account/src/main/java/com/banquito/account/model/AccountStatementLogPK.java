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

    @Column(name = "code_account_state_log", length = 10, nullable = false)
    private String codeAccountStateLog;

    @Column(name = "code_local_account", length = 20, nullable = false)
    private String codeLocalAccount;

    @Column(name = "code_international_account", length = 34, nullable = false)
    private String codeInternationalAccount;
}
