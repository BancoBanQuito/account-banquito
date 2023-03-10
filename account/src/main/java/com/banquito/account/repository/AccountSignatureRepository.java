package com.banquito.account.repository;

import com.banquito.account.model.Account;
import com.banquito.account.model.AccountClient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.banquito.account.model.AccountSignature;
import com.banquito.account.model.AccountSignaturePK;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountSignatureRepository extends JpaRepository<AccountSignature, AccountSignaturePK> {

    List<AccountSignature> findByPkCodeLocalAccount(String codeLocalAccount);

    List<AccountSignature> findByPkIdentificationTypeAndPkIdentification(String identificationType, String identification);

    Optional<AccountSignature> findByPkIdentificationTypeAndPkIdentificationAndPkCodeLocalAccount(
            String identificationType, String identification, String codeLocalAccount
    );
}
