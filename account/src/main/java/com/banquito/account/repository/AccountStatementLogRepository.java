package com.banquito.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.account.model.AccountStatementLog;
import com.banquito.account.model.AccountStatementLogPK;

@Repository
public interface AccountStatementLogRepository extends JpaRepository<AccountStatementLog, AccountStatementLogPK> {

}
