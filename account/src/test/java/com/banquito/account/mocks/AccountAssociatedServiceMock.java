package com.banquito.account.mocks;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import com.banquito.account.controller.dto.RSAccountAssociatedService;
import com.banquito.account.model.Account;
import com.banquito.account.model.AccountAssociatedService;
import com.banquito.account.model.AccountAssociatedServicePK;
import com.banquito.account.model.AccountPK;

public class AccountAssociatedServiceMock {

    /*
     * public static AccountAssociatedServicePK getAcountAssociatedServicePK() {
     * AccountAssociatedServicePK accountAssociatedServicePK = new
     * AccountAssociatedServicePK();
     * accountAssociatedServicePK.setCodeInternationalAccount(
     * "mockCodeInternationalAccount");
     * accountAssociatedServicePK.setCodeLocalAccount("mockCodeLocalAccount");
     * accountAssociatedServicePK.setCodeAssociatedService(
     * "mockCodeAssociatedService");
     * accountAssociatedServicePK.setCodeProduct("mockCodeProduct");
     * accountAssociatedServicePK.setCodeProductType("mockCodeProductType");
     * return accountAssociatedServicePK;
     * }
     * 
     * public static AccountAssociatedService getAccountAssociatedService() {
     * return AccountAssociatedService.builder()
     * .pk(getAcountAssociatedServicePK())
     * .status("mockStatus")
     * .startDate(new Date())
     * .build();
     * }
     */

    public static AccountAssociatedService mockAccountAssociatedService() {
        return AccountAssociatedService.builder()
                .pk(new AccountAssociatedServicePK("22cf89573e25a91bffbb", "db6dae82faeff5f13d9d0ecb6e0b7d5f49",
                        "4a169d2a0801710e895b0cb2abcfabdb", "6c24027751bc43c5b232242e307880a7", "ae3d5be3f318e8b7"))
                .status("ACT")
                .startDate(new Date())
                .build();
    }

    public static RSAccountAssociatedService getRSAccountAssociatedService() {

        return RSAccountAssociatedService.builder()
                .codeInternationalAccount("db6dae82faeff5f13d9d0ecb6e0b7d5f49")
                .codeLocalAccount("22cf89573e25a91bffbb")
                .codeAssociatedService("ae3d5be3f318e8b7")
                .status("ACT")
                .startDate(new Date())
                .build();
    }

    public static Optional<Account> mockAccount() {
        return Optional.of(Account.builder()
                .pk(new AccountPK("22cf89573e25a91bffbb", "db6dae82faeff5f13d9d0ecb6e0b7d5f49"))
                .codeProduct("4a169d2a0801710e895b0cb2abcfabdb").codeProductType("6c24027751bc43c5b232242e307880a7")
                .codeBranch("12345")
                .entityBankCode("4567").internationalBankCode("0123").status("act").createDate(new Date())
                .lastUpdateDate(new Date()).closeDate(new Date()).presentBalance(new BigDecimal(0.0))
                .availableBalance(new BigDecimal(0.0)).build());

    }

    public static Optional<AccountAssociatedService> mockOptionalAccountAssociatedService() {
        return Optional.of(mockAccountAssociatedService());
    }
}
