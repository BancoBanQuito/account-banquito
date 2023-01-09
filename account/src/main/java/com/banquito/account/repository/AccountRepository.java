package com.banquito.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.account.model.Account;
import com.banquito.account.model.AccountPK;

@Repository
public interface AccountRepository extends JpaRepository<Account, AccountPK> {

}
