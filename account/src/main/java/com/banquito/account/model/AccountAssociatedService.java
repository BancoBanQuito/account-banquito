package com.banquito.account.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.EqualsAndHashCode.Include;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "ACCOUNT_ASSOCIATED_SERVICE")
public class AccountAssociatedService implements Serializable {

	@EmbeddedId
	@Include
	private AccountAssociatedServicePK pk;

	@Column(name = "STATUS", length = 3, nullable = false)
	private String status;

	@Column(name = "START_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	@Column(name = "END_DATE", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	public AccountAssociatedService(AccountAssociatedServicePK pk) {
		this.pk = pk;
	}
}
