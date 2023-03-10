package com.banquito.account.schedule;

import com.banquito.account.model.Account;
import com.banquito.account.model.AccountStatementLog;
import com.banquito.account.repository.AccountRepository;
import com.banquito.account.repository.AccountStatementLogRepository;
import com.banquito.account.request.ProductRequest;
import com.banquito.account.request.TransactionRequest;
import com.banquito.account.request.dto.*;
import com.banquito.account.service.AccountStatementLogService;
import com.banquito.account.request.dto.RSProduct;
import com.banquito.account.utils.Utils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

@Component
public class ScheduledTasks {

    /*private final RSProduct winEveryDaySavings = RSProduct.builder().productType("6c24027751bc43c5b232242e307880a7")
            .id("4a169d2a0801710e895b0cb2abcfabdb").interest(BigDecimal.valueOf(4.25)).build();

    private final RSProduct standardSavings = RSProduct.builder().productType("6c24027751bc43c5b232242e307880a7")
            .id("ed3140ca2610b20511542066357b121f").interest(BigDecimal.valueOf(5.25)).build();

    private final RSProduct standardCurrent = RSProduct.builder().productType("bdc60173d3f0a82a1a04557e2d14ee32")
            .id("bc9699d3e5dafe9903b0dcd7e8778db1").interest(BigDecimal.valueOf(0)).build();

    private final RSProduct standardInvestment = RSProduct.builder().productType("be04e60db27fd509df44cfdb72dcfd74")
            .id("96d6161f059d93659aa1ef4662260768").interest(BigDecimal.valueOf(6.5)).build();

    private final RSProduct premiumInvestment = RSProduct.builder().productType("be04e60db27fd509df44cfdb72dcfd74")
            .id("344ac2fd48b5e4ea31851193523a26d9").interest(BigDecimal.valueOf(7.5)).build();

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);*/

    /*
        Executes every 5 seconds
        @Scheduled(cron = "0/5 * * * * *")
    */

    private final AccountRepository accountRepository;

    private final AccountStatementLogRepository accountLogRepository;

    private final AccountStatementLogService accountLogService;

    private final TransactionRequest transactionRequest;

    private final ProductRequest productRequest;

    public ScheduledTasks(AccountRepository accountRepository,
                          AccountStatementLogRepository accountLogRepository,
                          AccountStatementLogService accountLogService,
                          TransactionRequest transactionRequest, ProductRequest productRequest){
        this.accountRepository = accountRepository;
        this.accountLogRepository = accountLogRepository;
        this.accountLogService = accountLogService;
        this.transactionRequest = transactionRequest;
        this.productRequest = productRequest;
    }

    //Executes every day at 23:55
    //Savings accounts with daily capitalization
    @Scheduled(cron = "0 55 23 * * *")
    public void savingsDailyCapitalization() {

        List<RSProduct> raws = productRequest.getProducts();
        List<RSProduct> products = new ArrayList<>();

        for (RSProduct raw: raws){
            if(raw.getProductType().equals("Cuenta de ahorros")
                    && raw.getCapitalization().equals("Diario")
            ){
                products.add(raw);
            }
        }

        for( RSProduct product: products){
            List<Account> accounts = accountRepository.findByCodeProduct(product.getId());

            for (Account account : accounts) {

                if(!account.getStatus().equals("ACT")){
                    continue;
                }

                RQInterest interest = RQInterest.builder()
                        .codeLocalAccount(account.getPk().getCodeLocalAccount())
                        .codeInternationalAccount(account.getPk().getCodeInternationalAccount())
                        .ear(product.getInterest())
                        .baseCalc(product.getBaseCalc())
                        .availableBalance(account.getAvailableBalance())
                        .build();

                RSInterest responseInterest = transactionRequest.createSavingsAccountInterest(interest);

                if (responseInterest != null) {
                    Utils.saveLog(responseInterest, account.getPk().getCodeLocalAccount());

                    RQTransaction transaction = RQTransaction.builder()
                            .movement("NOTA CREDITO")
                            .type("INTERES")
                            .codeLocalAccount(account.getPk().getCodeLocalAccount())
                            .codeInternationalAccount(account.getPk().getCodeInternationalAccount())
                            .concept("Calculo interes, cuenta de ahorros GANA DIARIO")
                            .description("Capitalizacion de intereses de forma diaria")
                            .value(responseInterest.getValue())
                            .build();

                    RSTransaction responseTransaction = transactionRequest.createTransaction(transaction);

                    if(responseTransaction != null){
                        Utils.saveLog(responseTransaction, account.getPk().getCodeLocalAccount());
                    }
                }
            }
        }
    }

    //Savings accounts with monthly capitalization
    @Scheduled(cron = "0 55 23 * * *")
    public void savingsMonthlyCapitalization(){

        List<RSProduct> raws = productRequest.getProducts();
        List<RSProduct> products = new ArrayList<>();

        for (RSProduct raw: raws){
            if(raw.getProductType().equals("Cuenta de ahorros")
                    && raw.getCapitalization().equals("Mensual")
            ){
                products.add(raw);
            }
        }

        for( RSProduct product: products) {
            List<Account> accounts = accountRepository.findByCodeProduct(product.getId());

            for (Account account : accounts) {

                if(!account.getStatus().equals("ACT")){
                    continue;
                }

                RQInterest interest = RQInterest.builder()
                        .codeLocalAccount(account.getPk().getCodeLocalAccount())
                        .codeInternationalAccount(account.getPk().getCodeInternationalAccount())
                        .ear(product.getInterest())
                        .baseCalc(product.getBaseCalc())
                        .availableBalance(account.getAvailableBalance())
                        .build();

                RSInterest responseInterest = transactionRequest.createSavingsAccountInterest(interest);

                if (responseInterest != null) {
                    Utils.saveLog(responseInterest, account.getPk().getCodeLocalAccount());
                }
            }
        }
    }

    //Executes every day at 23:00
    @Scheduled(cron = "0 0 23 * * *")
    public void monthlyCapitalization(){

        List<RSProduct> raws = productRequest.getProducts();
        List<RSProduct> products = new ArrayList<>();

        for (RSProduct raw: raws){
            if(raw.getProductType().equals("Cuenta de ahorros")
                    && raw.getCapitalization().equals("Mensual")
            ){
                products.add(raw);
            }
        }

        for( RSProduct product: products) {

            List<Account> accounts = accountRepository.findByCodeProduct(product.getId());

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

                    List<RSInterest> dailyBalances = transactionRequest.getInterestBetweenDates(
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
                            .description("Capitalizacion de intereses de forma mensual")
                            .value(interest)
                            .build();

                    RSTransaction responseTransaction = transactionRequest.createTransaction(transaction);

                    if(responseTransaction != null){
                        Utils.saveLog(responseTransaction, account.getPk().getCodeLocalAccount());
                    }
                }
            }
        }
    }

    //Executes every day at 00:30
    @Scheduled(cron = "0 30 0 * * *")
    public void investmentInterest(){

        List<RSProduct> raws = productRequest.getProducts();
        List<RSProduct> products = new ArrayList<>();

        for (RSProduct raw: raws){
            if(raw.getProductType().equals("Inversiones")){
                products.add(raw);
            }
        }

        for( RSProduct product: products) {
            computeInvestmentInterest(product);
        }
    }

    public void computeInvestmentInterest(RSProduct product){
        List<Account> accounts = accountRepository.findByCodeProduct(product.getId());

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

                RSInvestment responseInvestment = transactionRequest.getInvestmentInterest(
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
                .description("Capitalizacion de intereses generada por inversion")
                .value(responseInvestment.getRawInterest())
                .build();

        RSTransaction responseCreditTransaction = transactionRequest.createTransaction(creditTransaction);

        if(responseCreditTransaction != null){
            Utils.saveLog(responseCreditTransaction, account.getPk().getCodeLocalAccount());

            RQTransaction debitTransaction = RQTransaction.builder()
                    .movement("NOTA DEBITO")
                    .type("PAGO")
                    .codeLocalAccount(account.getPk().getCodeLocalAccount())
                    .codeInternationalAccount(account.getPk().getCodeInternationalAccount())
                    .concept("Retencion 2% de inversion")
                    .description("Capitalizacion de intereses generada por inversion")
                    .value(responseInvestment.getRetention())
                    .build();

            RSTransaction responseDebitTransaction = transactionRequest.createTransaction(debitTransaction);

            if(responseCreditTransaction != null){
                Utils.saveLog(responseDebitTransaction, account.getPk().getCodeLocalAccount());
            }
        }
    }

    //Executes the 10th of every month at 00:10
    @Scheduled(cron = "0 10 0 10 * *")
    public void accountStatementSavingsDailyCapitalization(){
        List<RSProduct> raws = productRequest.getProducts();
        List<RSProduct> products = new ArrayList<>();

        for (RSProduct raw: raws){
            if(raw.getProductType().equals("Cuenta de ahorros") || raw.getProductType().equals("Cuenta corriente")){
                products.add(raw);
            }
        }

        for( RSProduct product: products) {
            computeAccountStatement(product);
        }
    }

    private void computeAccountStatement(RSProduct product){
        List<Account> accounts = accountRepository.findByCodeProduct(product.getId());

        for (Account account : accounts) {

            if(!account.getStatus().equals("ACT")){
                continue;
            }

            AccountStatementLog accountStatementLog = accountLogService.computeAccountStatement(account);

            try {
                this.accountLogRepository.save(accountStatementLog);
                Utils.saveLog(accountStatementLog,account.getPk().getCodeLocalAccount());

            } catch (Exception e) {
                Utils.saveLog(e, account.getPk().getCodeLocalAccount());
            }
        }
    }

}
