package com.banquito.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.account.model.AccountClient;
import com.banquito.account.model.AccountClientPK;

@Repository
public interface AccountClientRepository extends JpaRepository<AccountClient, AccountClientPK> {

    public boolean existsByPkCodeLocalAccountAndPkCodeInternationalAccount(String localAccountCode, String internationalAccountCode);
    public Optional<AccountClient> findByPkCodeLocalAccountAndPkCodeInternationalAccount(String localAccountCode, String internationalAccountCode);
}
