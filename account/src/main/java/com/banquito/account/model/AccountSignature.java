package com.banquito.account.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Temporal;
import jakarta.persistence.Table;
import jakarta.persistence.TemporalType;
<<<<<<< HEAD
import jakarta.persistence.Version;
=======
>>>>>>> 727a053a18c59e8d8993c5ca33e4e4066e353c44
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
	private long version;
    
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "code_local_account", referencedColumnName = "code_local_account", insertable = false, updatable = false),
            @JoinColumn(name = "code_international_account", referencedColumnName = "code_international_account", insertable = false, updatable = false),
    })
    private Account account;
    
    public AccountSignature(AccountSignaturePK pk) {
        this.pk = pk;
    }
}
