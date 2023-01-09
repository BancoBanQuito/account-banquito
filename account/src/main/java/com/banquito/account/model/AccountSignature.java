package com.banquito.account.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Temporal;
import jakarta.persistence.Table;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "account_signature")
public class AccountSignature implements Serializable{
    @EmbeddedId
    @Include
    private AccountSignaturePK pk;

    @Column(name = "signature_reference", length = 2048, nullable = false)
    private String signatureReference;

    @Column(name = "role", length = 64, nullable = false)
    private String role;

    @Column(name = "status", length = 3, nullable = false)
    private String status;

    @Temporal(TemporalType.DATE)
    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "end_date", nullable = true)
    private Date endDate;

    public AccountSignature(AccountSignaturePK pk) {
        this.pk = pk;
    }
}
