package com.banquito.account.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.account.model.AccountStatementLog;
import com.banquito.account.model.AccountStatementLogPK;

@Repository
public interface AccountStatementLogRepository extends JpaRepository<AccountStatementLog, AccountStatementLogPK> {
    public List<AccountStatementLog> findByPkCodeLocalAccountOrderByCurrentCutOffDateDesc(String localCodeAccount);

    public List<AccountStatementLog> findByPkCodeLocalAccountAndPkCodeInternationalAccount(
            String localCodeAccount, String internationalCodeAccount);

    public Optional<AccountStatementLog> findByPkCodeAccountStateLog(String codeAccountStateLog);
}
