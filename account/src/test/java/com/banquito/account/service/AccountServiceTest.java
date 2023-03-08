package com.banquito.account.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.banquito.account.controller.dto.RSAccount;
import com.banquito.account.controller.dto.RSProductTypeAndClientName;
import com.banquito.account.controller.mapper.AccountMapper;
import com.banquito.account.exception.RSRuntimeException;
import com.banquito.account.mocks.AccountClientMocks;
import com.banquito.account.mocks.AccountMocks;
import com.banquito.account.model.Account;
import com.banquito.account.model.AccountClient;
import com.banquito.account.model.AccountPK;
import com.banquito.account.repository.AccountClientRepository;
import com.banquito.account.repository.AccountRepository;
import com.banquito.account.request.ClientRequest;
import com.banquito.account.request.dto.RSClientSignature;

@SpringBootTest
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepositoryMock;

    @Mock
    private AccountClientRepository accountClientRepositoryMock;

    @Mock
    private ClientRequest clientRequest;

    @InjectMocks
    private AccountService accountServiceMock;

    @Test
    public void testCreateAccount() {
        Account expectedAccount = AccountMocks.getAccount();
        String mockIdentification = "mockIdentification";
        String mockIdentificationType = "mockIdentificationType";

        AccountClient expectedAccountClient = AccountClientMocks.getAccountClient();

        when(accountRepositoryMock.save(expectedAccount)).thenReturn(expectedAccount);
        when(accountClientRepositoryMock.save(expectedAccountClient)).thenReturn(expectedAccountClient);

        Account currentAccount = accountServiceMock.createAccount(
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

        when(accountClientRepositoryMock.findByPkIdentificationAndPkIdentificationType(
                mockIdentification,
                mockIdentificationType)).thenReturn(expectedAccountClients);
        when(accountRepositoryMock.findById(expectedAccountPK)).thenReturn(Optional.of(expectedAccount));

        List<RSAccount> currentRSAccounts = accountServiceMock.findAllAccountsByClient(
                mockIdentificationType,
                mockIdentification);

        assertEquals(currentRSAccounts, expectedRSAccounts);
    }

    @Test
    public void testFindAccountByCode() {
        String codeLocalAccount = "mockCodeLocalAccount";
        Account account = AccountMocks.getAccount();
        AccountClient accountClient = AccountClientMocks.getAccountClient();

        when(accountRepositoryMock.findByPkCodeLocalAccount(codeLocalAccount)).thenReturn(Optional.of(account));
        when(accountClientRepositoryMock.findByPkCodeLocalAccount(codeLocalAccount))
                .thenReturn(Optional.of(accountClient));

        RSAccount rsAccount = accountServiceMock.findAccountByCode(codeLocalAccount);

        assertEquals(rsAccount.getCodeLocalAccount(), account.getPk().getCodeLocalAccount());
        assertEquals(rsAccount.getIdentificationType(), accountClient.getPk().getIdentificationType());
        assertEquals(rsAccount.getIdentification(), accountClient.getPk().getIdentification());

        verify(accountRepositoryMock, times(1)).findByPkCodeLocalAccount(codeLocalAccount);
        verify(accountClientRepositoryMock, times(1)).findByPkCodeLocalAccount(codeLocalAccount);
    }

    @Test
    public void testFindAccountByCode_accountNotFound() {
        String codeLocalAccount = "mockCodeLocalAccount";

        when(accountRepositoryMock.findByPkCodeLocalAccount(codeLocalAccount)).thenReturn(Optional.empty());

        assertThrows(RSRuntimeException.class, () -> {
            accountServiceMock.findAccountByCode(codeLocalAccount);
        });

        verify(accountRepositoryMock, times(1)).findByPkCodeLocalAccount(codeLocalAccount);
        verifyNoInteractions(accountClientRepositoryMock);
        verifyNoInteractions(AccountMapper.class);
    }

    @Test
    public void testFindAccountByCode_accountClientNotFound() {
        String codeLocalAccount = "mockCodeLocalAccount";
        Account account = AccountMocks.getAccount();

        when(accountRepositoryMock.findByPkCodeLocalAccount(codeLocalAccount)).thenReturn(Optional.of(account));
        when(accountClientRepositoryMock.findByPkCodeLocalAccount(codeLocalAccount)).thenReturn(Optional.empty());

        assertThrows(RSRuntimeException.class, () -> {
            accountServiceMock.findAccountByCode(codeLocalAccount);
        });

        verify(accountRepositoryMock, times(1)).findByPkCodeLocalAccount(codeLocalAccount);
        verify(accountClientRepositoryMock, times(1)).findByPkCodeLocalAccount(codeLocalAccount);
        verifyNoInteractions(AccountMapper.class);
    }

    @Test
    public void testGetAccountProductTypeAndClientName_success() {
        String codeLocalAccount = "mockCodeLocalAccount";
        Account account = AccountMocks.getAccount();
        AccountClient accountClient = AccountClientMocks.getAccountClient();
        RSClientSignature clientData = new RSClientSignature(/* set client data properties */);

        when(accountRepositoryMock.findByPkCodeLocalAccount(codeLocalAccount)).thenReturn(Optional.of(account));
        when(accountClientRepositoryMock.findByPkCodeLocalAccount(codeLocalAccount))
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

        RSProductTypeAndClientName actual = accountServiceMock.getAccountProductTypeAndClientName(codeLocalAccount);

        assertEquals(expected, actual);

        verify(accountRepositoryMock, times(1)).findByPkCodeLocalAccount(codeLocalAccount);
        verify(accountClientRepositoryMock, times(1)).findByPkCodeLocalAccount(codeLocalAccount);
        verify(clientRequest, times(1)).getClientData(accountClient.getPk().getIdentificationType(),
                accountClient.getPk().getIdentification());
    }

    @Test
    public void testGetAccountProductTypeAndClientName_accountNotFound() {
        String codeLocalAccount = "mockCodeLocalAccount";

        when(accountRepositoryMock.findByPkCodeLocalAccount(codeLocalAccount)).thenReturn(Optional.empty());

        assertThrows(RSRuntimeException.class, () -> {
            accountServiceMock.getAccountProductTypeAndClientName(codeLocalAccount);
        });

        verify(accountRepositoryMock, times(1)).findByPkCodeLocalAccount(codeLocalAccount);
        verifyNoInteractions(accountClientRepositoryMock);
        verifyNoInteractions(clientRequest);
    }

    @Test
    public void testGetAccountProductTypeAndClientName_accountClientNotFound() {
        String codeLocalAccount = "mockCodeLocalAccount";
        Account account = AccountMocks.getAccount();

        when(accountRepositoryMock.findByPkCodeLocalAccount(codeLocalAccount)).thenReturn(Optional.of(account));
        when(accountClientRepositoryMock.findByPkCodeLocalAccount(codeLocalAccount)).thenReturn(Optional.empty());

        assertThrows(RSRuntimeException.class, () -> {
            accountServiceMock.getAccountProductTypeAndClientName(codeLocalAccount);
        });

        verify(accountRepositoryMock, times(1)).findByPkCodeLocalAccount(codeLocalAccount);
        verify(accountClientRepositoryMock, times(1)).findByPkCodeLocalAccount(codeLocalAccount);
        verifyNoInteractions(clientRequest);
    }
}
