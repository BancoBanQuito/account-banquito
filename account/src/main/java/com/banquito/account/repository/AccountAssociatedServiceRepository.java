package com.banquito.account.repository;

import com.banquito.account.model.AccountAssociatedService;
import com.banquito.account.model.AccountAssociatedServicePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountAssociatedServiceRepository extends JpaRepository<AccountAssociatedService, AccountAssociatedServicePK>{
    List<AccountAssociatedService> findByPkCodeLocalAccount(String codeLocalAccount);

    Optional<AccountAssociatedService> findByPkCodeLocalAccountAndPkCodeAssociatedService(
            String codeLocalAccount, String codeAssociatedService);
}
