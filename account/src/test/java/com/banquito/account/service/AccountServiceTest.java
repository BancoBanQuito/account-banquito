package com.banquito.account.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.banquito.account.mocks.AccountMocks;
import com.banquito.account.model.Account;
import com.banquito.account.model.AccountAssociatedService;
import com.banquito.account.model.AccountSignature;
import com.banquito.account.repository.AccountAssociatedServiceRepository;
import com.banquito.account.repository.AccountRepository;
import com.banquito.account.repository.AccountSignatureRepository;

@SpringBootTest
public class AccountServiceTest {

        @Mock
        private AccountRepository accountRepositoryMock;

        @Mock
        private AccountAssociatedServiceRepository accountAssociatedServiceMock;

        @Mock
        private AccountSignatureRepository accountSignatureRepositoryMock;

        @InjectMocks
        private AccountService accountServiceMock;

        @Test
        public void updateAccountBalanceTest() {
                Optional<Account> accountOpt = AccountMocks.mockAccount();
                String codeLocalAccount = "22cf89573e25a91bffbb";
                BigDecimal presentBalance = BigDecimal.valueOf(9999);
                BigDecimal availableBalance = BigDecimal.valueOf(9999);
                when(this.accountRepositoryMock
                                .findByPkCodeLocalAccount(codeLocalAccount)).thenReturn(accountOpt);
                Account account = AccountMocks
                                .getAccount();
                this.accountServiceMock
                                .updateAccountBalance(codeLocalAccount, presentBalance, availableBalance);
                assertEquals(account.getPresentBalance(), presentBalance);
        }

        @Test
        public void updateAccountStatus() {
                Optional<Account> accountOpt = AccountMocks.mockAccount();
                String codeLocalAccount = "22cf89573e25a91bffbb";
                String status = "ACT";

                when(this.accountRepositoryMock
                                .findByPkCodeLocalAccount(codeLocalAccount)).thenReturn(accountOpt);
                List<AccountAssociatedService> accountAssociatedServiceList = AccountMocks
                                .mockAccountAssociatedService();
                when(this.accountAssociatedServiceMock.findByPkCodeLocalAccount(codeLocalAccount))
                                .thenReturn(accountAssociatedServiceList);
                List<AccountSignature> accountSignatureList = AccountMocks
                                .mockAccountSignature();
                when(this.accountSignatureRepositoryMock.findByPkCodeLocalAccount(codeLocalAccount))
                                .thenReturn(accountSignatureList);
                Account account = AccountMocks
                                .getAccount();
                this.accountServiceMock.updateAccountStatus(codeLocalAccount, status);
                assertEquals(account.getStatus(), status);
        }

}
