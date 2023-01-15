package com.banquito.account.controller.mapper;

import com.banquito.account.controller.dto.RQCreateAccount;
import com.banquito.account.controller.dto.RSAccount;
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
                .codeinternationalaccount(account.getPk().getCodeInternationalAccount())
                .codelocalaccount(account.getPk().getCodeLocalAccount())
                .build();
    }

    public static RSAccount mapAccount(Account account) {
        return RSAccount.builder()
                .codeAccount(account.getPk().getCodeLocalAccount())
                .presentBalance(account.getPresentBalance())
                .availableBalance(account.getAvailableBalance())
                .build();
    }
}