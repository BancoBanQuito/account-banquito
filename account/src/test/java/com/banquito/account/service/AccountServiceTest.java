package com.banquito.account.service;

import com.banquito.account.controller.dto.RSAccount;
import com.banquito.account.controller.mapper.AccountMapper;
import com.banquito.account.mock.AccountServiceMock;
import com.banquito.account.model.Account;
import com.banquito.account.model.AccountClient;
import com.banquito.account.repository.AccountClientRepository;
import com.banquito.account.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;
    @Mock
    private AccountClientRepository accountClientRepository;

    @InjectMocks
    private  AccountService accountService;

    @Test
    public void findAccountByCode() {
        String accountCode = "codeLocal";
        Optional<Account> opAccount = AccountServiceMock.mockAccount(accountCode);
        Optional<AccountClient> opAccountClient = AccountServiceMock.mockAccountClient(accountCode);

        Account account = opAccount.get();
        AccountClient accountClient = opAccountClient.get();

        RSAccount expected = AccountMapper.mapAccount(account, accountClient.getPk().getIdentificationType(),
                accountClient.getPk().getIdentification());

        //When
        when(this.accountRepository.findByPkCodeLocalAccount(any(String.class))).thenReturn(opAccount);
        when(this.accountClientRepository.findByPkCodeLocalAccount(any(String.class))).thenReturn(opAccountClient);
        //Action
        RSAccount actual = this.accountService.findAccountByCode(accountCode);
        //Assert
        assertEquals(expected,actual);

    }
}