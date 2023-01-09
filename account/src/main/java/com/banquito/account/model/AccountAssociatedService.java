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
@Table(name = "account_associated_service")
public class AccountAssociatedService implements Serializable {

	@EmbeddedId
	@Include
	private AccountAssociatedServicePK pk;

	@Column(name = "code_associated_service", length = 16, nullable = false)
	private String codeAssociatedService;

	@Column(name = "status", length = 3, nullable = false)
	private String status;

	@Column(name = "start_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date startDate;

	@Column(name = "end_date", nullable = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date endDate;

	@Column(name = "code_local_account", length = 20, nullable = false, insertable = false, updatable = false)
	private String codeLocalAccount;

	@Column(name = "code_international_account", length = 34, nullable = false, insertable = false, updatable = false)
	private String codeInternationalAccount;

	@Column(name = "code_product", length = 32, nullable = false, insertable = false, updatable = false)
	private String codeProduct;

	@Column(name = "code_product_type", length = 32, nullable = false, insertable = false, updatable = false)
	private String codeProductType;

	public AccountAssociatedService(AccountAssociatedServicePK pk) {
		this.pk = pk;
	}
}
