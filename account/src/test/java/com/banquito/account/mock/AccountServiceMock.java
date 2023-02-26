package com.banquito.account.mock;

import com.banquito.account.model.Account;
import com.banquito.account.model.AccountClient;
import com.banquito.account.model.AccountClientPK;
import com.banquito.account.model.AccountPK;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

public class AccountServiceMock {

    public static Optional<Account> mockAccount(String codeLocalAccount){

        return Optional.of(Account.builder()
                        .pk(
                                AccountPK.builder()
                                        .codeLocalAccount("codeLocal")
                                        .codeInternationalAccount("codeInternational")
                                        .build()
                        )
                        .codeProduct("codeProduct")
                        .codeProductType("codeProductType")
                        .codeBranch("codeBranch")
                        .entityBankCode("bankLocal")
                        .internationalBankCode("bankInternational")
                        .status("ACT")
                        .createDate(new Date())
                        .lastUpdateDate(new Date())
                        .closeDate(new Date())
                        .presentBalance(BigDecimal.valueOf(10))
                        .availableBalance(BigDecimal.valueOf(10))
                        .build());
    }

    public static Optional<AccountClient> mockAccountClient(String codeLocalAccount){

        return Optional.of(
          AccountClient.builder()
                  .pk(
                          AccountClientPK.builder()
                                  .codeLocalAccount("codeLocal")
                                  .codeInternationalAccount("codeInternational")
                                  .identificationType("DNI")
                                  .identification("0123456789")
                                  .build()
                  )
                  .createDate(new Date())
                  .status("ACT")
                  .build());
    }
}
