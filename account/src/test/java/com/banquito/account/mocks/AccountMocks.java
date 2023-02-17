package com.banquito.account.mocks;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.banquito.account.model.Account;
import com.banquito.account.model.AccountAssociatedService;
import com.banquito.account.model.AccountAssociatedServicePK;
import com.banquito.account.model.AccountPK;
import com.banquito.account.model.AccountSignature;
import com.banquito.account.model.AccountSignaturePK;

public class AccountMocks {
    public static AccountPK getAccountPK() {
        return AccountPK.builder()
                .codeLocalAccount("22cf89573e25a91bffbb")
                .codeInternationalAccount("db6dae82faeff5f13d9d0ecb6e0b7d5f49")
                .build();
    }

    public static Account getAccount() {
        return Account.builder()
                .pk(getAccountPK())
                .codeProduct("4a169d2a0801710e895b0cb2abcfabdb")
                .codeProductType("6c24027751bc43c5b232242e307880a7")
                .codeBranch("12345")
                .entityBankCode("4567")
                .internationalBankCode("0123")
                .status("ACT")
                .createDate(new Date())
                .lastUpdateDate(new Date())
                .closeDate(new Date())
                .presentBalance(BigDecimal.valueOf(9999))
                .availableBalance(BigDecimal.valueOf(9999))
                .build();
    }

    public static Optional<Account> mockAccount() {
        return Optional.of(Account.builder()
                .pk(getAccountPK())
                .codeProduct("4a169d2a0801710e895b0cb2abcfabdb").codeProductType("6c24027751bc43c5b232242e307880a7")
                .codeBranch("12345")
                .entityBankCode("4567")
                .internationalBankCode("0123")
                .status("ACT").createDate(new Date())
                .lastUpdateDate(new Date())
                .closeDate(new Date())
                .presentBalance(new BigDecimal(0.0))
                .availableBalance(new BigDecimal(0.0))
                .build());

    }

    public static List<AccountAssociatedService> mockAccountAssociatedService() {
        return List.of(AccountAssociatedService.builder()
                .pk(new AccountAssociatedServicePK("22cf89573e25a91bffbb", "db6dae82faeff5f13d9d0ecb6e0b7d5f49",
                        "4a169d2a0801710e895b0cb2abcfabdb", "6c24027751bc43c5b232242e307880a7", "ae3d5be3f318e8b7"))
                .status("ACT")
                .startDate(new Date())
                .build());
    }

    public static List<AccountSignature> mockAccountSignature() {
        return List.of(AccountSignature.builder()
                .pk(new AccountSignaturePK("22cf89573e25a91bffbb", "db6dae82faeff5f13d9d0ecb6e0b7d5f49",
                        "DNI", "1727791731"))
                .signatureReference("ae3d5be3f318e8b7")
                .role("mockRole")
                .status("ACT")
                .createDate(new Date())
                .startDate(new Date())
                .build());
    }

}
