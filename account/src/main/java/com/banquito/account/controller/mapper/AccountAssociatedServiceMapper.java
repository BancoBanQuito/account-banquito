package com.banquito.account.controller.mapper;

import com.banquito.account.controller.dto.RQAccountAssociatedService;
import com.banquito.account.controller.dto.RSAccountAssociatedService;
import com.banquito.account.model.AccountAssociatedService;
import com.banquito.account.model.AccountAssociatedServicePK;

public class AccountAssociatedServiceMapper {

    public static RSAccountAssociatedService map(AccountAssociatedService accountAssociatedService) {
        return RSAccountAssociatedService.builder()
                .codeLocalAccount(accountAssociatedService.getPk().getCodeLocalAccount())
                .codeInternationalAccount(accountAssociatedService.getPk().getCodeInternationalAccount())
                .codeAssociatedService(accountAssociatedService.getPk().getCodeAssociatedService())
                .status(accountAssociatedService.getStatus())
                .startDate(accountAssociatedService.getStartDate())
                .build();
    }

    public static AccountAssociatedService map(RQAccountAssociatedService rqAccountAssociatedService){

        AccountAssociatedServicePK pk = new AccountAssociatedServicePK();
        pk.setCodeLocalAccount(rqAccountAssociatedService.getCodeLocalAccount());
        pk.setCodeProduct(rqAccountAssociatedService.getCodeProduct());
        pk.setCodeProductType(rqAccountAssociatedService.getCodeProductType());
        pk.setCodeAssociatedService(rqAccountAssociatedService.getCodeAssociatedService());

        return AccountAssociatedService.builder()
                .pk(pk)
                .build();

    }
}
