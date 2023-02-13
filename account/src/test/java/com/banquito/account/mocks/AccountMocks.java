package com.banquito.account.mocks;

import java.math.BigDecimal;
import java.util.Date;

import com.banquito.account.model.Account;
import com.banquito.account.model.AccountPK;

public class AccountMocks {

    public static AccountPK getAccountPK() {
        return AccountPK.builder()
                .codeLocalAccount("mockCodeLocalAccount")
                .codeInternationalAccount("mockCodeInternationalAccount")
                .build();
    }

    public static Account getAccount() {
        return Account.builder()
                .pk(getAccountPK())
                .codeProduct("mockCodeProduct")
                .codeProductType("mockCodeProductType")
                .codeBranch("mockCodeBranch")
                .entityBankCode("mockEntityBankCode")
                .internationalBankCode("mockInternationalBankCode")
                .status("mockStatus")
                .createDate(new Date())
                .lastUpdateDate(new Date())
                .closeDate(new Date())
                .presentBalance(BigDecimal.valueOf(9999))
                .availableBalance(BigDecimal.valueOf(9999))
                .build();
    }
}
