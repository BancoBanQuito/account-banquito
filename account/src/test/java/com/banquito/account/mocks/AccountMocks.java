package com.banquito.account.mocks;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.banquito.account.controller.dto.RSAccount;
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

    public static RSAccount getRSAccount() {
        return RSAccount.builder()
                .codeLocalAccount("mockCodeLocalAccount")
                .codeInternationalAccount("mockCodeInternationalAccount")
                .product("Sample")
                .presentBalance(BigDecimal.valueOf(9999))
                .availableBalance(BigDecimal.valueOf(9999))
                .build();
    }

    public static List<RSAccount> getRSAccounts() {
        List<RSAccount> RSAccounts = new ArrayList<RSAccount>();
        RSAccounts.add(getRSAccount());
        RSAccounts.add(getRSAccount());
        RSAccounts.add(getRSAccount());
        RSAccounts.add(getRSAccount());

        return RSAccounts;
    }

}
