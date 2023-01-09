package com.banquito.account.model;

import lombok.Data;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Data
@Embeddable
public class AccountAssociatedServicePK implements Serializable {

        @Column(name = "code_local_account", length = 20, nullable = false)
        private String codelocalaccount;

        @Column(name = "code_international_account", length = 34, nullable = false)
        private String codeinternationalaccount;

        @Column(name = "code_product", length = 32, nullable = false)
        private String codeproduct;

        @Column(name = "code_product_type", length = 32, nullable = false)
        private String codeproducttype;

}
