package com.banquito.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.account.model.AccountAssociatedService;
import com.banquito.account.model.AccountAssociatedServicePK;

@Repository
public interface AccountAssociatedServiceRepository extends JpaRepository<AccountAssociatedService, AccountAssociatedServicePK>{
    
}
