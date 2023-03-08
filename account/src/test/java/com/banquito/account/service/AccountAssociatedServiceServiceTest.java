package com.banquito.account.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.banquito.account.controller.dto.RSAccountAssociatedService;
import com.banquito.account.mocks.AccountAssociatedServiceMock;
import com.banquito.account.model.Account;
import com.banquito.account.model.AccountAssociatedService;
import com.banquito.account.repository.AccountAssociatedServiceRepository;
import com.banquito.account.repository.AccountRepository;

@SpringBootTest
public class AccountAssociatedServiceServiceTest {

        @Mock
        private AccountRepository accountRepositoryMock;

        @Mock
        private AccountAssociatedServiceRepository accountAssociatedServiceRepositoryMock;

        @InjectMocks
        private AccountAssociatedServiceService accountAssociatedServiceServiceMock;

        @Test
        public void createAssociatedServiceTest() {
                Optional<Account> accountOpt = AccountAssociatedServiceMock.mockAccount();
                RSAccountAssociatedService expected = AccountAssociatedServiceMock.getRSAccountAssociatedService();
                // System.out.println(accountOpt.get().getPk().getCodeInternationalAccount());
                when(this.accountRepositoryMock.findByPkCodeLocalAccount(any(String.class))).thenReturn(accountOpt);
                AccountAssociatedService accountAssociatedService = AccountAssociatedServiceMock
                                .mockAccountAssociatedService();
                RSAccountAssociatedService response = this.accountAssociatedServiceServiceMock
                                .createAssociatedService(accountAssociatedService);
                assertEquals(expected.getCodeAssociatedService(), response.getCodeAssociatedService());
        }

        @Test
        public void updateAssociatedServiceTest() {
                Optional<AccountAssociatedService> accountOptAccountAssoicatedService = AccountAssociatedServiceMock
                                .mockOptionalAccountAssociatedService();
                String codeLocalAccount = "22cf89573e25a91bffbb";
                String codeAssociatedService = "ae3d5be3f318e8b7";
                String status = "ACT";
                when(this.accountAssociatedServiceRepositoryMock
                                .findByPkCodeLocalAccountAndPkCodeAssociatedService(any(String.class),
                                                any(String.class)))
                                .thenReturn(accountOptAccountAssoicatedService);
                AccountAssociatedService accountAssociatedService = AccountAssociatedServiceMock
                                .mockAccountAssociatedService();
                this.accountAssociatedServiceServiceMock
                                .updateAssociatedServiceStatus(codeLocalAccount, codeAssociatedService, status);
                assertTrue(accountAssociatedService.getStatus().equals(status));
        }
}
