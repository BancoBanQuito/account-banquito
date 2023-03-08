package com.banquito.account.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataAccessException;
import org.springframework.test.annotation.Rollback;

import com.banquito.account.controller.dto.RSAccount;
import com.banquito.account.controller.dto.RSProductTypeAndClientName;
import com.banquito.account.controller.mapper.AccountMapper;
import com.banquito.account.exception.RSRuntimeException;
import com.banquito.account.mocks.AccountClientMocks;
import com.banquito.account.mocks.AccountMocks;
import com.banquito.account.model.Account;
import com.banquito.account.model.AccountAssociatedService;
import com.banquito.account.model.AccountAssociatedServicePK;
import com.banquito.account.model.AccountClient;
import com.banquito.account.model.AccountPK;
import com.banquito.account.model.AccountSignature;
import com.banquito.account.model.AccountSignaturePK;
import com.banquito.account.repository.AccountAssociatedServiceRepository;
import com.banquito.account.repository.AccountClientRepository;
import com.banquito.account.repository.AccountRepository;
import com.banquito.account.repository.AccountSignatureRepository;
import com.banquito.account.request.ClientRequest;
import com.banquito.account.request.dto.RSClientSignature;

@SpringBootTest
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountClientRepository accountClientRepository;

    @Mock
    private AccountAssociatedServiceRepository accountAssociatedServiceRepository;

    @Mock
    private AccountSignatureRepository accountSignatureRepository;

    @Mock
    private ClientRequest clientRequest;

    @InjectMocks
    private AccountService accountService;

    @Test
    public void testCreateAccount() {
        Account expectedAccount = AccountMocks.getAccount();
        String mockIdentification = "mockIdentification";
        String mockIdentificationType = "mockIdentificationType";

        AccountClient expectedAccountClient = AccountClientMocks.getAccountClient();

        when(accountRepository.save(expectedAccount)).thenReturn(expectedAccount);
        when(accountClientRepository.save(expectedAccountClient)).thenReturn(expectedAccountClient);

        Account currentAccount = accountService.createAccount(
                expectedAccount,
                mockIdentification,
                mockIdentificationType);

        assertEquals(currentAccount, expectedAccount);
    }

    @Test
    public void testFindAllAccountsByClient() {
        String mockIdentification = "mockIdentification";
        String mockIdentificationType = "mockIdentificationType";

        List<AccountClient> expectedAccountClients = AccountClientMocks.getAccountClients();
        List<RSAccount> expectedRSAccounts = AccountMocks.getRSAccounts();
        AccountPK expectedAccountPK = AccountMocks.getAccountPK();
        Account expectedAccount = AccountMocks.getAccount();

        when(accountClientRepository.findByPkIdentificationAndPkIdentificationType(
                mockIdentification,
                mockIdentificationType)).thenReturn(expectedAccountClients);
        when(accountRepository.findById(expectedAccountPK)).thenReturn(Optional.of(expectedAccount));

        List<RSAccount> currentRSAccounts = accountService.findAllAccountsByClient(
                mockIdentificationType,
                mockIdentification);

        assertEquals(currentRSAccounts, expectedRSAccounts);
    }

    @Test
    public void testFindAccountByCode() {
        String codeLocalAccount = "mockCodeLocalAccount";
        Account account = AccountMocks.getAccount();
        AccountClient accountClient = AccountClientMocks.getAccountClient();

        when(accountRepository.findByPkCodeLocalAccount(codeLocalAccount)).thenReturn(Optional.of(account));
        when(accountClientRepository.findByPkCodeLocalAccount(codeLocalAccount))
                .thenReturn(Optional.of(accountClient));

        RSAccount rsAccount = accountService.findAccountByCode(codeLocalAccount);

        assertEquals(rsAccount.getCodeLocalAccount(), account.getPk().getCodeLocalAccount());
        assertEquals(rsAccount.getIdentificationType(), accountClient.getPk().getIdentificationType());
        assertEquals(rsAccount.getIdentification(), accountClient.getPk().getIdentification());

        verify(accountRepository, times(1)).findByPkCodeLocalAccount(codeLocalAccount);
        verify(accountClientRepository, times(1)).findByPkCodeLocalAccount(codeLocalAccount);
    }

    @Test
    public void testFindAccountByCode_accountNotFound() {
        String codeLocalAccount = "mockCodeLocalAccount";

        when(accountRepository.findByPkCodeLocalAccount(codeLocalAccount)).thenReturn(Optional.empty());

        assertThrows(RSRuntimeException.class, () -> {
            accountService.findAccountByCode(codeLocalAccount);
        });

        verify(accountRepository, times(1)).findByPkCodeLocalAccount(codeLocalAccount);
    }

    @Test
    public void testFindAccountByCode_accountClientNotFound() {
        String codeLocalAccount = "mockCodeLocalAccount";
        Account account = AccountMocks.getAccount();

        when(accountRepository.findByPkCodeLocalAccount(codeLocalAccount)).thenReturn(Optional.of(account));
        when(accountClientRepository.findByPkCodeLocalAccount(codeLocalAccount)).thenReturn(Optional.empty());

        assertThrows(RSRuntimeException.class, () -> {
            accountService.findAccountByCode(codeLocalAccount);
        });

        verify(accountRepository, times(1)).findByPkCodeLocalAccount(codeLocalAccount);
        verify(accountClientRepository, times(1)).findByPkCodeLocalAccount(codeLocalAccount);
    }

    @Test
    public void testGetAccountProductTypeAndClientName_success() {
        String codeLocalAccount = "mockCodeLocalAccount";
        Account account = AccountMocks.getAccount();
        AccountClient accountClient = AccountClientMocks.getAccountClient();
        RSClientSignature clientData = new RSClientSignature(/* set client data properties */);

        when(accountRepository.findByPkCodeLocalAccount(codeLocalAccount)).thenReturn(Optional.of(account));
        when(accountClientRepository.findByPkCodeLocalAccount(codeLocalAccount))
                .thenReturn(Optional.of(accountClient));
        when(clientRequest.getClientData(accountClient.getPk().getIdentificationType(),
                accountClient.getPk().getIdentification()))
                .thenReturn(clientData);

        RSProductTypeAndClientName expected = RSProductTypeAndClientName.builder()
                .codeLocalAccount(codeLocalAccount)
                .productType(account.getCodeProductType())
                .product(account.getCodeProduct())
                .identificationType(accountClient.getPk().getIdentificationType())
                .identification(accountClient.getPk().getIdentification())
                .name(clientData.getName() + " " + clientData.getLastName())
                .build();

        RSProductTypeAndClientName actual = accountService.getAccountProductTypeAndClientName(codeLocalAccount);

        assertEquals(expected, actual);

        verify(accountRepository, times(1)).findByPkCodeLocalAccount(codeLocalAccount);
        verify(accountClientRepository, times(1)).findByPkCodeLocalAccount(codeLocalAccount);
        verify(clientRequest, times(1)).getClientData(accountClient.getPk().getIdentificationType(),
                accountClient.getPk().getIdentification());
    }

    @Test
    public void testGetAccountProductTypeAndClientName_accountNotFound() {
        String codeLocalAccount = "mockCodeLocalAccount";

        when(accountRepository.findByPkCodeLocalAccount(codeLocalAccount)).thenReturn(Optional.empty());

        assertThrows(RSRuntimeException.class, () -> {
            accountService.getAccountProductTypeAndClientName(codeLocalAccount);
        });

        verify(accountRepository, times(1)).findByPkCodeLocalAccount(codeLocalAccount);
        verifyNoInteractions(accountClientRepository);
        verifyNoInteractions(clientRequest);
    }

    @Test
    public void testGetAccountProductTypeAndClientName_accountClientNotFound() {
        String codeLocalAccount = "mockCodeLocalAccount";
        Account account = AccountMocks.getAccount();

        when(accountRepository.findByPkCodeLocalAccount(codeLocalAccount)).thenReturn(Optional.of(account));
        when(accountClientRepository.findByPkCodeLocalAccount(codeLocalAccount)).thenReturn(Optional.empty());

        assertThrows(RSRuntimeException.class, () -> {
            accountService.getAccountProductTypeAndClientName(codeLocalAccount);
        });

        verify(accountRepository, times(1)).findByPkCodeLocalAccount(codeLocalAccount);
        verify(accountClientRepository, times(1)).findByPkCodeLocalAccount(codeLocalAccount);
        verifyNoInteractions(clientRequest);
    }

    @Test
    @Rollback
    void testUpdateAccountStatus() {
        // Mock Account object
        Account account = new Account();
        account.setPk(new AccountPK("123", "456"));
        account.setStatus("ACT");

        // Mock AccountAssociatedService object
        AccountAssociatedService service = new AccountAssociatedService();
        service.setPk(new AccountAssociatedServicePK("123", "789", null, null, null));
        service.setStatus("ACT");

        // Mock AccountSignature object
        AccountSignature signature = new AccountSignature();
        signature.setPk(new AccountSignaturePK("123", "012", null, null));
        signature.setStatus("ACT");

        // Mock repositories
        when(accountRepository.findByPkCodeLocalAccount("123")).thenReturn(Optional.of(account));
        when(accountAssociatedServiceRepository.findByPkCodeLocalAccount("123"))
                .thenReturn(Collections.singletonList(service));
        when(accountSignatureRepository.findByPkCodeLocalAccount("123"))
                .thenReturn(Collections.singletonList(signature));

        // Call the function
        accountService.updateAccountStatus("123", "BLO");

        // Verify that the account status is updated
        verify(accountRepository, times(1)).save(account);
        Assertions.assertEquals("BLO", account.getStatus());

        // Verify that the associated service status is updated
        verify(accountAssociatedServiceRepository, times(1)).save(service);
        Assertions.assertEquals("BLO", service.getStatus());

        // Verify that the signature status is updated
        verify(accountSignatureRepository, times(1)).save(signature);
        Assertions.assertEquals("BLO", signature.getStatus());
    }

    @Test
    void testUpdateAccountBalance_shouldUpdateBalance() {
        // Arrange
        String codeLocalAccount = "123456";
        BigDecimal presentBalance = new BigDecimal("1000.00");
        BigDecimal availableBalance = new BigDecimal("800.00");
        Account account = new Account();
        account.setPk(new AccountPK(codeLocalAccount, codeLocalAccount));
        account.setPresentBalance(new BigDecimal("500.00"));
        account.setAvailableBalance(new BigDecimal("400.00"));
        when(accountRepository.findByPkCodeLocalAccount(codeLocalAccount)).thenReturn(Optional.of(account));

        // Act
        accountService.updateAccountBalance(codeLocalAccount, presentBalance, availableBalance);

        // Assert
        assertEquals(presentBalance, account.getPresentBalance());
        assertEquals(availableBalance, account.getAvailableBalance());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void testUpdateAccountBalance_shouldThrowExceptionWhenAccountNotFound() {
        // Arrange
        String codeLocalAccount = "123456";
        BigDecimal presentBalance = new BigDecimal("1000.00");
        BigDecimal availableBalance = new BigDecimal("800.00");
        when(accountRepository.findByPkCodeLocalAccount(codeLocalAccount)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RSRuntimeException.class, () -> {
            accountService.updateAccountBalance(codeLocalAccount, presentBalance, availableBalance);
        });
        verify(accountRepository, never()).save(any(Account.class));
    }

}
