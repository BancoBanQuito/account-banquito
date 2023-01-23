package com.banquito.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.account.model.AccountAssociatedService;
import com.banquito.account.model.AccountAssociatedServicePK;

import java.util.List;

@Repository
public interface AccountAssociatedServiceRepository extends JpaRepository<AccountAssociatedService, AccountAssociatedServicePK>{
    List<AccountAssociatedService> findByPkCodeLocalAccountAndPkCodeInternationalAccount(String codeLocalAccount, String codeInternationalAccount);

}
