package com.banquito.account.service;

import com.banquito.account.controller.dto.RSAccount;
import com.banquito.account.controller.dto.RSProductTypeAndClientName;
import com.banquito.account.controller.mapper.AccountMapper;
import com.banquito.account.exception.RSRuntimeException;
import com.banquito.account.model.*;
import com.banquito.account.repository.AccountAssociatedServiceRepository;
import com.banquito.account.repository.AccountClientRepository;
import com.banquito.account.repository.AccountRepository;
import com.banquito.account.repository.AccountSignatureRepository;
import com.banquito.account.request.ClientRequest;
import com.banquito.account.request.ProductRequest;
import com.banquito.account.request.TransactionRequest;
import com.banquito.account.request.dto.*;
import com.banquito.account.utils.Messages;
import com.banquito.account.utils.RSCode;
import com.banquito.account.utils.Status;
import com.banquito.account.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountClientRepository accountClientRepository;
    private final AccountSignatureRepository accountSignatureRepository;
    private final AccountAssociatedServiceRepository accountAssociatedServiceRepository;
    private final ClientRequest clientRequest;
    private final ProductRequest productRequest;
    private final TransactionRequest transactionRequest;

    public AccountService(AccountRepository accountRepository,
                          AccountClientRepository accountClientRepository,
                          AccountSignatureRepository accountSignatureRepository,
                          AccountAssociatedServiceRepository accountAssociatedServiceRepository,
                          ClientRequest clientRequest, ProductRequest productRequest, TransactionRequest transactionRequest) {
        this.accountRepository = accountRepository;
        this.accountClientRepository = accountClientRepository;
        this.accountSignatureRepository = accountSignatureRepository;
        this.accountAssociatedServiceRepository = accountAssociatedServiceRepository;
        this.clientRequest = clientRequest;
        this.productRequest = productRequest;
        this.transactionRequest = transactionRequest;
    }

    @Transactional
    public Account createAccount(Account account, String identification, String identificationType) {

        String localAccountCode = Utils.generateNumberCode(20);
        String internationalAccountCode = Utils.generateNumberCode(34);
        AccountPK accountPK = new AccountPK();
        accountPK.setCodeInternationalAccount(internationalAccountCode);
        accountPK.setCodeLocalAccount(localAccountCode);
        account.setPk(accountPK);
        account.setStatus(Status.ACTIVATE.code);
        account.setCreateDate(Utils.currentDate());
        account.setLastUpdateDate(Utils.currentDate());
        account.setAvailableBalance(new BigDecimal(0));
        account.setPresentBalance(new BigDecimal(0));

        log.info(account.toString());

        AccountClientPK accountClientPK = new AccountClientPK();
        accountClientPK.setCodeInternationalAccount(internationalAccountCode);
        accountClientPK.setCodeLocalAccount(localAccountCode);
        accountClientPK.setIdentification(identification);
        accountClientPK.setIdentificationType(identificationType);

        AccountClient accountClient = AccountClient.builder()
                .pk(accountClientPK)
                .status(Status.ACTIVATE.code)
                .createDate(Utils.currentDate())
                .build();
        try {
            this.accountRepository.save(account);
            this.accountClientRepository.save(accountClient);
        } catch (Exception e) {
            throw new RSRuntimeException(Messages.ACCOUNT_NOT_CREATED, RSCode.INTERNAL_SERVER_ERROR);
        }
        return account;
    }

    public List<RSAccount> findAllAccountsByClient(String identificationType, String identification) {
        List<RSAccount> rsAccounts = new ArrayList<>();
        List<AccountClient> accountClients = new ArrayList<>();

        accountClients = this.accountClientRepository
                .findByPkIdentificationAndPkIdentificationType(identification, identificationType);
        log.info("" + accountClients.size());
        if (accountClients.size() < 1) {
            throw new RSRuntimeException(Messages.ACCOUNTS_NOT_FOUND_FOR_CLIENT, RSCode.NOT_FOUND);
        }

        try {
            accountClients.forEach(accountClient -> {
                AccountPK pk = new AccountPK();
                pk.setCodeLocalAccount(accountClient.getPk().getCodeLocalAccount());
                pk.setCodeInternationalAccount(accountClient.getPk().getCodeInternationalAccount());
                Optional<Account> optionalAccount = this.accountRepository.findById(pk);
                if (optionalAccount.isPresent()) {
                    Account account = optionalAccount.get();

                    /* Pedir el producto con un api */
                    String status = getAccountStatus(account.getStatus());
                    RSAccount rsAccount = RSAccount.builder()
                            .codeLocalAccount(account.getPk().getCodeLocalAccount())
                            .codeInternationalAccount(account.getPk().getCodeInternationalAccount())
                            .status(status)
                            .product("Sample")
                            .presentBalance(account.getPresentBalance())
                            .availableBalance(account.getAvailableBalance())
                            .build();
                    rsAccounts.add(rsAccount);
                }
            });
        } catch (Exception e) {
            throw new RSRuntimeException(Messages.INTERNAL_ERROR, RSCode.INTERNAL_SERVER_ERROR);
        }

        return rsAccounts;
    }

    private String getAccountStatus(String status) {
        if (status.equals(Status.ACTIVATE.code)) {
            return Status.ACTIVATE.name;
        } else if (status.equals(Status.BLOCKED.code)) {
            return Status.BLOCKED.name;
        } else if (status.equals(Status.SUSPEND.code)) {
            return Status.SUSPEND.name;
        } else {
            return null;
        }
    }

    public RSAccount findAccountByCode(String codeLocalAccount){

        Optional<Account> opAccount = accountRepository.findByPkCodeLocalAccount(codeLocalAccount);

        if(!opAccount.isPresent()){
            throw new RSRuntimeException(Messages.ACCOUNTS_NOT_FOUND_FOR_CODE, RSCode.NOT_FOUND);
        }

        Account account = opAccount.get();

        //account client is required to get client id
        Optional<AccountClient> opClientAccount = accountClientRepository.findByPkCodeLocalAccount(codeLocalAccount);

        if(!opClientAccount.isPresent()){
            throw new RSRuntimeException(Messages.ACCOUNTS_NOT_FOUND_FOR_CODE, RSCode.NOT_FOUND);
        }

        AccountClient accountClient = opClientAccount.get();


        return AccountMapper.mapAccount(account,
                accountClient.getPk().getIdentificationType(),
                accountClient.getPk().getIdentification());
    }

    public RSProductTypeAndClientName getAccountProductTypeAndClientName(String codeLocalAccount){


        Optional<Account> opAccount = accountRepository.findByPkCodeLocalAccount(codeLocalAccount);

        if(!opAccount.isPresent()){
            throw new RSRuntimeException(Messages.ACCOUNTS_NOT_FOUND_FOR_CODE, RSCode.NOT_FOUND);
        }

        Account account = opAccount.get();

        //account client is required to get client id
        Optional<AccountClient> opClientAccount = accountClientRepository.findByPkCodeLocalAccount(codeLocalAccount);

        if(!opClientAccount.isPresent()){
            throw new RSRuntimeException(Messages.ACCOUNTS_NOT_FOUND_FOR_CODE, RSCode.NOT_FOUND);
        }

        AccountClient accountClient = opClientAccount.get();

        //api call to client module
        RSClientSignature clientData = clientRequest.getClientData(
                accountClient.getPk().getIdentificationType(), accountClient.getPk().getIdentification());

        if(clientData == null){
            throw new RSRuntimeException(Messages.CLIENT_NOT_FOUND, RSCode.BAD_REQUEST);
        }

        return RSProductTypeAndClientName.builder()
                .codeLocalAccount(codeLocalAccount)
                .productType(account.getCodeProductType())
                .product(account.getCodeProduct())
                .identificationType(accountClient.getPk().getIdentificationType())
                .identification(accountClient.getPk().getIdentification())
                .name(clientData.getName() + " " + clientData.getLastName())
                .build();
    }

    @Transactional
    public void updateAccountStatus(String codeLocalAccount, String status){

        Optional<Account> opAccount = accountRepository.findByPkCodeLocalAccount(codeLocalAccount);

        if(!opAccount.isPresent()){
            throw new RSRuntimeException(Messages.ACCOUNTS_NOT_FOUND_FOR_CODE, RSCode.NOT_FOUND);
        }

        Account account = opAccount.get();

        account.setStatus(status);

        List<AccountAssociatedService> services = accountAssociatedServiceRepository.findByPkCodeLocalAccount(codeLocalAccount);

        if(services.size() > 0){
            if(status.equals("BLO")||status.equals("SUS")||status.equals("INA")){
                for(AccountAssociatedService service: services){
                    service.setStatus(status);
                    try {
                        this.accountAssociatedServiceRepository.save(service);
                    } catch (Exception e) {
                        throw new RSRuntimeException(Messages.SERVICE_NOT_UPDATED, RSCode.INTERNAL_SERVER_ERROR);
                    }
                }
            }
        }

        List<AccountSignature> signatures = accountSignatureRepository.findByPkCodeLocalAccount(codeLocalAccount);

        if(signatures.size() > 0){
            if(status.equals("BLO")||status.equals("SUS")||status.equals("INA")){
                for(AccountSignature signature: signatures){
                    signature.setStatus(status);
                    try {
                        this.accountSignatureRepository.save(signature);
                    } catch (Exception e) {
                        throw new RSRuntimeException(Messages.SIGNATURE_NOT_UPDATED, RSCode.INTERNAL_SERVER_ERROR);
                    }
                }
            }
        }

        try {
            this.accountRepository.save(account);
        } catch (Exception e) {
            throw new RSRuntimeException(Messages.ACCOUNT_NOT_UPDATED, RSCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void updateAccountBalance(String codeLocalAccount, BigDecimal presentBalance,BigDecimal availableBalance){

        Optional<Account> opAccount = accountRepository.findByPkCodeLocalAccount(codeLocalAccount);

        if(!opAccount.isPresent()){
            throw new RSRuntimeException(Messages.ACCOUNTS_NOT_FOUND_FOR_CODE, RSCode.NOT_FOUND);
        }

        Account account = opAccount.get();

        account.setAvailableBalance(availableBalance);
        account.setPresentBalance(presentBalance);

        try {
            this.accountRepository.save(account);
        } catch (Exception e) {
            throw new RSRuntimeException(Messages.ACCOUNT_NOT_UPDATED, RSCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public List<Object> computeInterest(String codeLocalAccount){

        List<Object> responses = new ArrayList<>();

        Optional<Account> opAccount = accountRepository.findByPkCodeLocalAccount(codeLocalAccount);

        if(!opAccount.isPresent()){
            throw new RSRuntimeException(Messages.ACCOUNTS_NOT_FOUND_FOR_CODE, RSCode.NOT_FOUND);
        }

        Account account = opAccount.get();

        List<RSProduct> raws = productRequest.getProducts();
        RSProduct product = null;

        for (RSProduct raw: raws){
            if(raw.getId().equals(account.getCodeProduct())){
                product = raw;
            }
        }

        if(!account.getStatus().equals("ACT")){
            throw new RSRuntimeException(Messages.ACCOUNT_NOT_ACTIVE, RSCode.NOT_FOUND);
        }

        if(product != null && product.getProductType().equals("Cuenta de ahorros")){
            RQInterest interest = RQInterest.builder()
                    .codeLocalAccount(account.getPk().getCodeLocalAccount())
                    .codeInternationalAccount(account.getPk().getCodeInternationalAccount())
                    .ear(product.getInterest())
                    .baseCalc(product.getBaseCalc())
                    .availableBalance(account.getAvailableBalance())
                    .build();

            RSInterest responseInterest = transactionRequest.createSavingsAccountInterest(interest);

            if (responseInterest != null) {

                responses.add(responseInterest);

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
                    responses.add(responseTransaction);
                }

            }
        }else if(product != null && product.getProductType().equals("Inversiones")){

            SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = sm.format(Utils.currentDate());

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
                    responses.add(responseInvestment);

                    RQTransaction creditTransaction = RQTransaction.builder()
                            .movement("NOTA CREDITO")
                            .type("INTERES")
                            .codeLocalAccount(account.getPk().getCodeLocalAccount())
                            .codeInternationalAccount(account.getPk().getCodeInternationalAccount())
                            .concept("Calculo interes, inversion estandar")
                            .description("Capitalizacion de intereses generada por inversion")
                            .value(responseInvestment.getRawInterest())
                            .build();

                    RSTransaction responseCreditTransaction  = transactionRequest.createTransaction(creditTransaction);
                    responses.add(responseCreditTransaction);

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
                            responses.add(responseDebitTransaction);
                        }
                    }
                }
            }

        }
        else {
            throw new RSRuntimeException("Product not found", RSCode.NOT_FOUND);
        }

        return responses;
    }
}