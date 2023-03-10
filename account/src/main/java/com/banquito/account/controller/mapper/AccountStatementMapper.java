package com.banquito.account.controller.mapper;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

import com.banquito.account.controller.dto.RSAccountStatement;
import com.banquito.account.controller.dto.RSAccountStatementList;
import com.banquito.account.controller.dto.RSAccountStatementTransactions;
import com.banquito.account.model.AccountClient;
import com.banquito.account.model.AccountStatementLog;
import com.banquito.account.request.dto.RSTransaction;

public class AccountStatementMapper {

    public static RSAccountStatement map(AccountStatementLog accountStatement,
                                         String identificationType,
                                         String identification,
                                         String fullname
                                        ){

        return RSAccountStatement.builder()
                .localCodeAccount(accountStatement.getPk().getCodeLocalAccount())
                .identificationType(identificationType)
                .identification(identification)
                .fullName(fullname)
                .lastCutOffDate(
                        LocalDateTime.ofInstant(accountStatement.getLastCutOffDate().toInstant(), ZoneId.systemDefault())
                )
                .currentCutOffDate(
                        LocalDateTime.ofInstant(accountStatement.getCurrentCutOffDate().toInstant(), ZoneId.systemDefault())
                )
                .previousBalance(accountStatement.getPreviousBalance())
                .creditMovements(accountStatement.getCreditMovements())
                .debitMovements(accountStatement.getDebitMovements())
                .interest(accountStatement.getInterest())
                .currentBalance(accountStatement.getCurrentBalance())
                .averageBalance(accountStatement.getAverageCashBalance())
                .build();
    }


    public static RSAccountStatementTransactions map(RSTransaction transaction){

        return RSAccountStatementTransactions.builder()
                .date(transaction.getExecuteDate())
                .movement(transaction.getMovement())
                .concept(transaction.getConcept())
                .amount(transaction.getValue())
                .balance(transaction.getAvailableBalance())
                .build();
    }

    public static RSAccountStatementList mapList(AccountStatementLog accountStatementLog){

        return RSAccountStatementList.builder()
                .code(accountStatementLog.getPk().getCodeAccountStateLog())
                .currentCutOffDate(accountStatementLog.getCurrentCutOffDate())
                .credit(accountStatementLog.getCreditMovements())
                .debit(accountStatementLog.getDebitMovements())
                .balance(accountStatementLog.getCurrentBalance())
                .interest(accountStatementLog.getInterest())
                .build();
    }
}
