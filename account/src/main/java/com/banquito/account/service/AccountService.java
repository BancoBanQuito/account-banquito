package com.banquito.account.service;

import com.banquito.account.controller.dto.RSAccount;
import com.banquito.account.exception.RSRuntimeException;
import com.banquito.account.model.*;
import com.banquito.account.repository.AccountAssociatedServiceRepository;
import com.banquito.account.repository.AccountClientRepository;
import com.banquito.account.repository.AccountRepository;
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
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountClientRepository accountClientRepository;
    private final AccountAssociatedServiceRepository accountAssociatedServiceRepository;

    public AccountService(AccountRepository accountRepository,
                          AccountClientRepository accountClientRepository,
                          AccountAssociatedServiceRepository accountAssociatedServiceRepository) {
        this.accountRepository = accountRepository;
        this.accountClientRepository = accountClientRepository;
        this.accountAssociatedServiceRepository = accountAssociatedServiceRepository;
    }

    //The  test service is only used for development purposes
    public Object test() {

        List<Account> accounts = accountRepository.findByCodeProductAndCodeProductType(
                "aed99062a63c327876956943ae41dd36", "239368d97b07f1459ae208d520d3db27");

        return "DONE";
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

    public Account findAccountByCode(String codeLocalAccount, String codeInternationalAccount){
        Optional<Account> opAccount = accountRepository.findById(
                AccountPK.builder()
                        .codeLocalAccount(codeLocalAccount)
                        .codeInternationalAccount(codeInternationalAccount)
                        .build()
        );

        if(!opAccount.isPresent()){
            throw new RSRuntimeException(Messages.ACCOUNTS_NOT_FOUND_FOR_CODE, RSCode.NOT_FOUND);
        }

        return opAccount.get();
    }

    @Transactional
    public void updateAccountStatus(String codeLocalAccount, String codeInternationalAccount, String status){

        Optional<Account> opAccount = accountRepository.findById(
                AccountPK.builder()
                        .codeLocalAccount(codeLocalAccount)
                        .codeInternationalAccount(codeInternationalAccount)
                        .build()
        );

        if(!opAccount.isPresent()){
            throw new RSRuntimeException(Messages.ACCOUNTS_NOT_FOUND_FOR_CODE, RSCode.NOT_FOUND);
        }

        Account account = opAccount.get();

        account.setStatus(status);

        List<AccountAssociatedService> services = accountAssociatedServiceRepository.
                findByPkCodeLocalAccountAndPkCodeInternationalAccount(codeLocalAccount,codeInternationalAccount);

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

        try {
            this.accountRepository.save(account);
        } catch (Exception e) {
            throw new RSRuntimeException(Messages.ACCOUNT_NOT_UPDATED, RSCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Transactional
    public void updateAccountBalance(String codeLocalAccount, String codeInternationalAccount,
                                     BigDecimal presentBalance,BigDecimal availableBalance){

        Optional<Account> opAccount = accountRepository.findById(
                AccountPK.builder()
                        .codeLocalAccount(codeLocalAccount)
                        .codeInternationalAccount(codeInternationalAccount)
                        .build()
        );

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
}