package com.banquito.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.account.model.AccountClient;
import com.banquito.account.model.AccountClientPK;

@Repository
public interface AccountClientRepository extends JpaRepository<AccountClient, AccountClientPK> {

}
