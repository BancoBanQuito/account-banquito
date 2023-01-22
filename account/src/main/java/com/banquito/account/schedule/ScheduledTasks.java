package com.banquito.account.schedule;

import com.banquito.account.controller.dto.RSAccountStatement;
import com.banquito.account.exception.RSRuntimeException;
import com.banquito.account.model.Account;
import com.banquito.account.model.AccountStatementLog;
import com.banquito.account.repository.AccountRepository;
import com.banquito.account.repository.AccountStatementLogRepository;
import com.banquito.account.request.TransactionRequest;
import com.banquito.account.request.dto.*;
import com.banquito.account.service.AccountStatementLogService;
import com.banquito.account.utils.Messages;
import com.banquito.account.utils.Product;
import com.banquito.account.utils.RSCode;
import com.banquito.account.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;

@Component
public class ScheduledTasks {

    private final Product winEveryDaySavings = Product.builder().code("savingsOne")
            .type("savings").interest(BigDecimal.valueOf(5.75)).build();

    private final Product standardSavings = Product.builder().code("savingsTwo")
            .type("savings").interest(BigDecimal.valueOf(6.50)).build();

    private final Product standardCurrent = Product.builder().code("currentOne")
            .type("current").interest(BigDecimal.valueOf(0)).build();

    private final Product standardInvestment = Product.builder().code("investmentOne")
            .type("investment").interest(BigDecimal.valueOf(6.5)).build();

    private final Product premiumInvestment = Product.builder().code("investmentTwo")
            .type("investment").interest(BigDecimal.valueOf(7.5)).build();

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    /*
        Executes every 5 seconds
        @Scheduled(cron = "0/5 * * * * *")
    */

    private final AccountRepository accountRepository;

    private final AccountStatementLogRepository accountLogRepository;

    private final AccountStatementLogService accountLogService;

    public ScheduledTasks(AccountRepository accountRepository,
                          AccountStatementLogRepository accountLogRepository,
                          AccountStatementLogService accountLogService){
        this.accountRepository = accountRepository;
        this.accountLogRepository = accountLogRepository;
        this.accountLogService = accountLogService;
    }

    //Executes every day at 23:55
    @Scheduled(cron = "0 55 23 * * *")
    public void winEveryDaySavingsInterest() {
        //Cuentas de ahorros gana diario, el interes se capitaliza todos los dias
        List<Account> accounts = accountRepository.findByCodeProductAndCodeProductType(
                winEveryDaySavings.getCode(), winEveryDaySavings.getType());

        for (Account account : accounts) {

            if(!account.getStatus().equals("ACT")){
                continue;
            }

            RQInterest interest = RQInterest.builder()
                    .codeLocalAccount(account.getPk().getCodeLocalAccount())
                    .codeInternationalAccount(account.getPk().getCodeInternationalAccount())
                    .ear(winEveryDaySavings.getInterest())
                    .availableBalance(account.getAvailableBalance())
                    .build();

            RSInterest responseInterest = TransactionRequest.createSavingsAccountInterest(interest);

            if (responseInterest != null) {
                Utils.saveLog(responseInterest, account.getPk().getCodeLocalAccount());

                RQTransaction transaction = RQTransaction.builder()
                        .movement("NOTA CREDITO")
                        .type("INTERES")
                        .codeLocalAccount(account.getPk().getCodeLocalAccount())
                        .codeInternationalAccount(account.getPk().getCodeInternationalAccount())
                        .concept("Calculo interes, cuenta de ahorros GANA DIARIO")
                        .value(responseInterest.getValue())
                        .build();

                RSTransaction responseTransaction = TransactionRequest.createTransaction(transaction);

                if(responseTransaction != null){
                    Utils.saveLog(responseTransaction, account.getPk().getCodeLocalAccount());
                }
            }
        }
    }

    @Scheduled(cron = "0 55 23 * * *")
    public void standardSavingsInterest(){
        //Cuentas de ahorro estandar, el interes se capitaliza al generar el estado de cuenta el dia 10 de cada mes
        List<Account> accounts = accountRepository.findByCodeProductAndCodeProductType(
                standardSavings.getCode(), standardSavings.getType());

        for (Account account : accounts) {

            if(!account.getStatus().equals("ACT")){
                continue;
            }

            RQInterest interest = RQInterest.builder()
                    .codeLocalAccount(account.getPk().getCodeLocalAccount())
                    .codeInternationalAccount(account.getPk().getCodeInternationalAccount())
                    .ear(standardSavings.getInterest())
                    .availableBalance(account.getAvailableBalance())
                    .build();

            RSInterest responseInterest = TransactionRequest.createSavingsAccountInterest(interest);

            if (responseInterest != null) {
                Utils.saveLog(responseInterest, account.getPk().getCodeLocalAccount());
            }
        }
    }

    //Executes every day at 23:00
    @Scheduled(cron = "0 0 23 * * *")
    public void standardSavingsInterestCapitalization(){
        List<Account> accounts = accountRepository.findByCodeProductAndCodeProductType(
                standardSavings.getCode(), standardSavings.getType());

        LocalDateTime currentDayOfMonth = OffsetDateTime.now().toLocalDateTime();

        LocalDateTime firstDayOfMonth = OffsetDateTime.now().toLocalDateTime().with(TemporalAdjusters.firstDayOfMonth());

        LocalDateTime lastDayOfMonth = OffsetDateTime.now().toLocalDateTime().with(TemporalAdjusters.lastDayOfMonth());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


        if(formatter.format(currentDayOfMonth).equals(formatter.format(lastDayOfMonth))){
            for (Account account : accounts) {

                if(!account.getStatus().equals("ACT")){
                    continue;
                }

                BigDecimal interest = BigDecimal.valueOf(0);

                List<RSInterest> dailyBalances = TransactionRequest.getInterestBetweenDates(
                        account.getPk().getCodeLocalAccount(),
                        firstDayOfMonth,
                        lastDayOfMonth
                );

                if(dailyBalances.size() > 0){
                    for(RSInterest dailyBalance: dailyBalances){
                        interest = interest.add(dailyBalance.getValue());
                    }
                }

                RQTransaction transaction = RQTransaction.builder()
                        .movement("NOTA CREDITO")
                        .type("INTERES")
                        .codeLocalAccount(account.getPk().getCodeLocalAccount())
                        .codeInternationalAccount(account.getPk().getCodeInternationalAccount())
                        .concept("Calculo interes, cuenta de ahorros STANDARD")
                        .value(interest)
                        .build();

                RSTransaction responseTransaction = TransactionRequest.createTransaction(transaction);

                if(responseTransaction != null){
                    Utils.saveLog(responseTransaction, account.getPk().getCodeLocalAccount());
                }
            }
        }
    }

    //Executes every day at 00:30
    @Scheduled(cron = "0 30 0 * * *")
    public void standardInvestmentInterest(){
        //Cuentas de inversion estandar, valores menores a 50.000
       computeInterestInvestment(standardInvestment);
    }

    @Scheduled(cron = "0 30 0 * * *")
    public void premiumInvestmentInterest(){
        //Cuentas de inversion premiun, valores mayores a 50.000
        computeInterestInvestment(premiumInvestment);
    }

    private void computeInterestInvestment(Product product){
        List<Account> accounts = accountRepository.findByCodeProductAndCodeProductType(
                product.getCode(), product.getType());

        SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = sm.format(Utils.currentDate());

        for (Account account : accounts) {

            if(!account.getStatus().equals("ACT")){
                continue;
            }

            String closeDate = sm.format(account.getCloseDate());

            if(currentDate.equals(closeDate)){

                LocalDate startDate = LocalDate.parse(sm.format(account.getCreateDate()));
                LocalDate endDate = LocalDate.parse(closeDate);

                long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

                RSInvestment responseInvestment = TransactionRequest.getInvestmentInterest(
                        account.getPk().getCodeLocalAccount(),
                        Math.toIntExact(daysBetween),
                        account.getAvailableBalance(),
                        product.getInterest()
                );

                if(responseInvestment != null){
                    Utils.saveLog(responseInvestment, account.getPk().getCodeLocalAccount());
                    generateTransactionsInvestmentInterest(account, responseInvestment);
                }
            }
        }
    }

    private void generateTransactionsInvestmentInterest(Account account, RSInvestment responseInvestment){

        RQTransaction creditTransaction = RQTransaction.builder()
                .movement("NOTA CREDITO")
                .type("INTERES")
                .codeLocalAccount(account.getPk().getCodeLocalAccount())
                .codeInternationalAccount(account.getPk().getCodeInternationalAccount())
                .concept("Calculo interes, inversion estandar")
                .value(responseInvestment.getRawInterest())
                .build();

        RSTransaction responseCreditTransaction = TransactionRequest.createTransaction(creditTransaction);

        if(responseCreditTransaction != null){
            Utils.saveLog(responseCreditTransaction, account.getPk().getCodeLocalAccount());

            RQTransaction debitTransaction = RQTransaction.builder()
                    .movement("NOTA DEBITO")
                    .type("INTERES")
                    .codeLocalAccount(account.getPk().getCodeLocalAccount())
                    .codeInternationalAccount(account.getPk().getCodeInternationalAccount())
                    .concept("Retencion 2% de inversion")
                    .value(responseInvestment.getRetention())
                    .build();

            RSTransaction responseDebitTransaction = TransactionRequest.createTransaction(debitTransaction);

            if(responseCreditTransaction != null){
                Utils.saveLog(responseDebitTransaction, account.getPk().getCodeLocalAccount());
            }
        }
    }

    //Executes the 10th of every month at 00:10
    @Scheduled(cron = "0 10 0 10 * *")
    public void accountStatementWinEveryDaySavings(){
        computeAccountStatement(winEveryDaySavings);
    }

    //Executes the 10th of every month at 01:10
    @Scheduled(cron = "0 10 1 10 * *")
    public void accountStatementStandardSavings(){
        computeAccountStatement(standardSavings);
    }


    //Executes the 10th of every month at 02:10
    @Scheduled(cron = "0 10 2 10 * *")
    public void accountStatementCurrentAccount(){
        computeAccountStatement(standardCurrent);
    }

    private void computeAccountStatement(Product product){
        List<Account> accounts = accountRepository.findByCodeProductAndCodeProductType(
                product.getCode(), product.getType());

        for (Account account : accounts) {

            AccountStatementLog accountStatementLog = accountLogService.getAccountStatement(account);

            try {
                this.accountLogRepository.save(accountStatementLog);
                Utils.saveLog(accountStatementLog,account.getPk().getCodeLocalAccount());

            } catch (Exception e) {
                Utils.saveLog(e, account.getPk().getCodeLocalAccount());
            }
        }
    }

}
