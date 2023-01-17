package com.banquito.account.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.banquito.account.utils.Utils;
import com.banquito.account.utils.AccountStatusCode;
import com.banquito.account.utils.RSCode;
import com.banquito.account.controller.dto.RSAccount;
import com.banquito.account.exception.RSRuntimeException;
import com.banquito.account.model.Account;
import com.banquito.account.model.AccountClient;
import com.banquito.account.model.AccountClientPK;
import com.banquito.account.model.AccountPK;
import com.banquito.account.repository.AccountClientRepository;
import com.banquito.account.repository.AccountRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final AccountClientRepository accountClientRepository;

    private final String ACCOUNT_NOT_CREATED = "Ha ocurrido un error al crear la cuenta";
    private final String NOT_ENOUGH_PARAM = "Faltan parametros en la peticion";
    private final String NOT_FOUND_ACCOUNTS = "No hay cuentas asociadas al cliente";
    private final String INTERNAL_ERROR = "Ha ocurrido un error";

    public AccountService(AccountRepository accountRepository, AccountClientRepository accountClientRepository) {
        this.accountRepository = accountRepository;
        this.accountClientRepository = accountClientRepository;
    }

    @Transactional
    public Account createAccount(Account account, String identification, String identificationType) {
        if (identification.isEmpty() || identificationType.isEmpty() || account.equals(null)) {
            throw new RSRuntimeException(this.NOT_ENOUGH_PARAM, RSCode.NOT_FOUND);
        }

        String localAccountCode = Utils.generateNumberCode(20);
        String internationalAccountCode = Utils.generateNumberCode(34);
        AccountPK accountPK = new AccountPK();
        accountPK.setCodeInternationalAccount(internationalAccountCode);
        accountPK.setCodeLocalAccount(localAccountCode);
        account.setPk(accountPK);
        account.setStatus(AccountStatusCode.ACTIVATE.code);
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
                .status(AccountStatusCode.ACTIVATE.code)
                .createDate(Utils.currentDate())
                .build();
        try {
            this.accountRepository.save(account);
            this.accountClientRepository.save(accountClient);
        } catch (Exception e) {
            throw new RSRuntimeException(this.ACCOUNT_NOT_CREATED, RSCode.INTERNAL_ERROR_SERVER);
        }
        return account;
    }

    public List<RSAccount> findAllAccountsByClient(String identificationType, String identification) {
        List<RSAccount> rsAccounts = new ArrayList<>();
        List<AccountClient> accountClients = new ArrayList<>();

        accountClients = this.accountClientRepository
                .findByPkIdentificationAndPkIdentificationType(identification, identificationType);
        log.info("" + accountClients.size());
        if (accountClients.size() <= 0) {
            throw new RSRuntimeException(this.NOT_FOUND_ACCOUNTS, RSCode.NOT_FOUND);
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
                            .codeAccount(account.getPk().getCodeLocalAccount())
                            .status(status)
                            .product("Sample")
                            .presentBalance(account.getPresentBalance())
                            .availableBalance(account.getAvailableBalance())
                            .build();
                    rsAccounts.add(rsAccount);
                }
            });
        } catch (Exception e) {
            throw new RSRuntimeException(this.INTERNAL_ERROR, RSCode.INTERNAL_ERROR_SERVER);
        }

        return rsAccounts;
    }

    private String getAccountStatus(String status) {
        if (status.equals(AccountStatusCode.ACTIVATE.code)) {
            return AccountStatusCode.ACTIVATE.name;
        } else if (status.equals(AccountStatusCode.BLOCKED.code)) {
            return AccountStatusCode.BLOCKED.name;
        } else if (status.equals(AccountStatusCode.SUSPEND.code)) {
            return AccountStatusCode.SUSPEND.name;
        } else {
            return null;
        }
    }
}