package com.banquito.account.controller.mapper;

import com.banquito.account.controller.dto.RQCreateAccount;
import com.banquito.account.controller.dto.RSCreateAccount;
import com.banquito.account.model.Account;

public class AccountMapper {

    public static Account map(RQCreateAccount account) {
        return Account.builder()
                .codeBranch(account.getCodeBranch())
                .entityBankCode(account.getEntityBankCode())
                .internationalBankCode(account.getInternationalBankCode())
                .codeProduct(account.getCodeProduct())
                .codeProductType(account.getCodeProductType())
                .build();
    }

    public static RSCreateAccount map(Account account) {
        return RSCreateAccount.builder()
                .codeinternationalaccount(account.getPk().getCodeinternationalaccount())
                .codelocalaccount(account.getPk().getCodelocalaccount())
                .build();
    }
}
