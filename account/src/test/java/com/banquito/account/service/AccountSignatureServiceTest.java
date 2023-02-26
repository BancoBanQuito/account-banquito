package com.banquito.account.service;

import com.banquito.account.repository.AccountSignatureRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

class AccountSignatureServiceTest {

    @Mock
    private AccountSignatureRepository accountSignatureRepository;

    @InjectMocks
    private AccountSignatureService accountSignatureService;

    @Test
    void findSignaturesById() {
        String id = "1750343210";


    }
}