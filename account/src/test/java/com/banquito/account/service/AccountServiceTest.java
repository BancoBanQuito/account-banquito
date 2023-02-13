package com.banquito.account.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.banquito.account.mocks.AccountClientMocks;
import com.banquito.account.mocks.AccountMocks;
import com.banquito.account.model.Account;
import com.banquito.account.model.AccountClient;
import com.banquito.account.repository.AccountClientRepository;
import com.banquito.account.repository.AccountRepository;

@SpringBootTest
public class AccountServiceTest {
    @Mock
    private AccountRepository accountRepositoryMock;

    @Mock
    private AccountClientRepository accountClientRepositoryMock;

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
}
