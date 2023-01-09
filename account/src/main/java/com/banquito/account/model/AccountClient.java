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
@Table(name = "account_client")
public class AccountClient implements Serializable{
    @EmbeddedId
    @Include
    private AccountClientPK pk;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_date", nullable = false)
    private Date createDate;

    @Column(name = "status", length = 3, nullable = false)
    private String status;

    public AccountClient(AccountClientPK pk) {
        this.pk = pk;
    }
}
