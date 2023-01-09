package com.banquito.account.model;

import lombok.Data;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Data
@Embeddable
public class AccountAssociatedServicePK implements Serializable {

        @Column(name = "CODE_LOCAL_ACCOUNT", length = 20, nullable = false)
        private String codeLocalAccount;

        @Column(name = "CODE_INTERNATIONAL_ACCOUNT", length = 34, nullable = false)
        private String codeInternationalAccount;

        @Column(name = "CODE_PRODUCT", length = 32, nullable = false)
        private String codeProduct;

        @Column(name = "CODE_PRODUCT_TYPE", length = 32, nullable = false)
        private String codeProductType;

        @Column(name = "CODE_ASSOCIATED_SERVICE", length = 16, nullable = false)
        private String codeAssociatedService;

}
