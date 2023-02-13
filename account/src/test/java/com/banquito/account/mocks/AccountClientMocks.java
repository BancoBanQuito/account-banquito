package com.banquito.account.mocks;

import java.util.Date;

import com.banquito.account.model.AccountClient;
import com.banquito.account.model.AccountClientPK;

public class AccountClientMocks {

    public static AccountClientPK getAcountClientPK() {
        AccountClientPK accountClientPK = new AccountClientPK();
        accountClientPK.setCodeInternationalAccount("mockCodeInternationalAccount");
        accountClientPK.setCodeLocalAccount("mockCodeLocalAccount");
        accountClientPK.setIdentification("mockIdentification");
        accountClientPK.setIdentificationType("mockIdentificationType");

        return accountClientPK;
    }

    public static AccountClient getAccountClient() {
        return AccountClient.builder()
                .pk(getAcountClientPK())
                .createDate(new Date())
                .status("mockStatus")
                .build();
    }

}
