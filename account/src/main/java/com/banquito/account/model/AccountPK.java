package com.banquito.account.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class AccountPK implements Serializable  {
    
    @Column(name = "code_local_account", length = 20, nullable = false)
    private String codeLocalAccount;

    @Column(name = "code_international_account", length = 34, nullable = false)
    private String codeInternationalAccount;
}
