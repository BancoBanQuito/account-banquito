package com.banquito.account.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
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

    @Version
	private Long version;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "code_local_account", referencedColumnName = "code_local_account", insertable = false, updatable = false),
            @JoinColumn(name = "code_international_account", referencedColumnName = "code_international_account", insertable = false, updatable = false),
    })
    private Account account;
}
