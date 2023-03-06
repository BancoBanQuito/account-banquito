package com.banquito.account.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.platform.commons.annotation.Testable;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.banquito.account.controller.dto.RSSignature;
import com.banquito.account.service.AccountSignatureService;
import com.banquito.account.utils.RSFormat;

@Testable
public class AccountSignatureTest {
    public void testGetSignatureListByCode() {
        AccountSignatureService accountSignatureServiceMock = Mockito.mock(AccountSignatureService.class);

        String codeLocalAccount = "123456789";
        List<RSSignature> signatures = new ArrayList<>();
        signatures.add(new RSSignature());

        when(accountSignatureServiceMock.findSignaturesByCode(codeLocalAccount)).thenReturn(signatures);

        AccountSignatureController accountSignatureController = new AccountSignatureController(accountSignatureServiceMock);

        ResponseEntity<RSFormat<List<RSSignature>>> response = accountSignatureController.getSignatureListByCode(codeLocalAccount);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Success", response.getBody().getMessage());
        assertEquals(signatures, response.getBody().getData());
    }
}
