package com.banquito.account.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.account.model.AccountClient;
import com.banquito.account.model.AccountClientPK;

@Repository
public interface AccountClientRepository extends JpaRepository<AccountClient, AccountClientPK> {

    public boolean existsByPkCodeLocalAccountAndPkCodeInternationalAccount(String localAccountCode, String internationalAccountCode);
    public List<AccountClient> findByPkCodeLocalAccountAndPkCodeInternationalAccount(String localAccountCode, String internationalAccountCode);
    public List<AccountClient> findByPkIdentificationAndPkIdentificationType(String identification, String identificationType);
    public Optional<AccountClient> findByPkCodeLocalAccountOrPkCodeInternationalAccount(String accountLocalCode, String accountInternationalCode);
}
