package com.banquito.account.model;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Data
@Embeddable
public class AccountSignaturePK implements Serializable{

        @Column(name = "code_local_account", length = 20, nullable = false)
        private String codeLocalAccount;

        @Column(name = "code_international_account", length = 34, nullable = false)
        private String codeInternationalAccount;

        @Column(name = "identification_type", length = 3, nullable = false)
        private String identificationType;

        @Column(name = "identification", length = 20, nullable = false)
        private String identification;
}
