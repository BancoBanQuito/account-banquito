package com.banquito.account.model;

import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Data
@Embeddable
public class AccountPK implements Serializable  {
    
    @Column(name = "code_local_account", length = 20, nullable = false)
    private String codeLocalAccount;

    @Column(name = "code_international_account", length = 34, nullable = false)
    private String codeInternationalAccount;
}
