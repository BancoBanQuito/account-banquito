package com.banquito.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.account.model.AccountSignature;
import com.banquito.account.model.AccountSignaturePK;

@Repository
public interface AccountSignatureRepository extends JpaRepository<AccountSignature, AccountSignaturePK> {

}
