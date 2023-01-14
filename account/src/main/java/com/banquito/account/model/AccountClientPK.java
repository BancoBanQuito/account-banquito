package com.banquito.account.model;

import java.io.Serializable;
import lombok.Data;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Data
@Embeddable
public class AccountClientPK implements Serializable{
        @Column(name = "code_local_account", length = 20, nullable = false)
        private String codeLocalAccount;

        @Column(name = "code_international_account", length = 34, nullable = false)
        private String codeInternationalAccount;

        @Column(name = "identification_type", length = 3, nullable = false)
        private String identificationType;

        @Column(name = "identification", length = 20, nullable = false)
        private String identification;
}
