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

	@Version
	private Long version;

	@ManyToOne
	@JoinColumns({
			@JoinColumn(name = "code_local_account", referencedColumnName = "code_local_account", insertable = false, updatable = false),
			@JoinColumn(name = "code_international_account", referencedColumnName = "code_international_account", insertable = false, updatable = false),
	})
	private Account account;
}
