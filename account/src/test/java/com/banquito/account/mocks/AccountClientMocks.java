package com.banquito.account.mocks;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    public static List<AccountClient> getAccountClients() {
        List<AccountClient> accountClients = new ArrayList<AccountClient>();
        accountClients.add(getAccountClient());
        accountClients.add(getAccountClient());
        accountClients.add(getAccountClient());
        accountClients.add(getAccountClient());

        return accountClients;
    }

}
