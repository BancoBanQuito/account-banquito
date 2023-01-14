package com.banquito.account.controller.mapper;

import java.util.List;

import com.banquito.account.controller.dto.RSAccountStatement;
import com.banquito.account.model.AccountClient;
import com.banquito.account.model.AccountStatementLog;

public class AccountStatementMapper {

    public static RSAccountStatement map(String fullname, AccountClient accountClient, AccountStatementLog accountStatementLog,
            List<RSAccountStatement.Transaction> transactions) {
        RSAccountStatement rsAccountStatement = RSAccountStatement.builder()
                .fullname(fullname)
                .accountCode(accountClient.getPk().getCodeLocalAccount())
                .clientIdentification(accountClient.getPk().getIdentification())
                .lastCutOffDate(accountStatementLog.getLastCutOffDate())
                .actualCutOffDate(accountStatementLog.getPk().getActualCutOffDate())
                .lastBalance(accountStatementLog.getLastBalance())
                .interestRate(accountStatementLog.getInterestRate())
                .presentBalance(accountStatementLog.getActualBalance())
                .promBalance(accountStatementLog.getPromBalance())
                .transactions(transactions)
                .build();

        return rsAccountStatement;
    }
}
