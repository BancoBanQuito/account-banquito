package com.banquito.account.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.banquito.account.controller.dto.RSSignature;
import com.banquito.account.exception.RSRuntimeException;
import com.banquito.account.mock.AccountSignatureMock;
import com.banquito.account.model.AccountSignature;
import com.banquito.account.model.AccountSignaturePK;
import com.banquito.account.repository.AccountSignatureRepository;
import com.banquito.account.request.ClientRequest;
import com.banquito.account.request.dto.RSClientSignature;
import com.banquito.account.utils.Messages;
import com.banquito.account.utils.RSCode;

@SpringBootTest
public class AccountSignatureTest {

    @InjectMocks
    private AccountSignatureService signatureService;

    @Mock
    private AccountSignatureRepository accountSignatureRepository;

    @Mock
    private ClientRequest clientRequest;

        @Test
        public void testFindSignaturesByCode_whenSignaturesExist_shouldReturnSignatures() {
            AccountSignatureMock signatureMock = new AccountSignatureMock();
            String codeLocalAccount = "1234";
            List<AccountSignature> dbSignatures = new ArrayList<>();
            AccountSignature dbSignature = signatureMock.createSignature();
            dbSignatures.add(dbSignature);
            when(accountSignatureRepository.findByPkCodeLocalAccount(codeLocalAccount)).thenReturn(dbSignatures);
            
            RSClientSignature clientSignature = new RSClientSignature();
            clientSignature.setName("John");
            clientSignature.setLastName("Doe");
            when(clientRequest.getClientData(dbSignature.getPk().getIdentificationType(), dbSignature.getPk().getIdentification())).thenReturn(clientSignature);
            
            List<RSSignature> signatures = signatureService.findSignaturesByCode(codeLocalAccount);
            
            assertNotNull(signatures);
            assertEquals(1, signatures.size());
            RSSignature signature = signatures.get(0);
            assertEquals("John Doe", signature.getName());
        }
        
        @Test
        public void testFindSignaturesByCode_whenSignaturesDoNotExist_shouldThrowRSRuntimeException() {
            String codeLocalAccount = "1234";
            when(accountSignatureRepository.findByPkCodeLocalAccount(codeLocalAccount)).thenReturn(new ArrayList<>());
            
            RSRuntimeException exception = assertThrows(RSRuntimeException.class, () -> {
                signatureService.findSignaturesByCode(codeLocalAccount);
            });
            
            assertEquals(Messages.ACCOUNTS_NOT_FOUND_FOR_CODE, exception.getMessage());
        }
        
        @Test
        public void testFindSignaturesByCode_whenClientNotFound_shouldThrowRSRuntimeException() {
            AccountSignatureMock signatureMock = new AccountSignatureMock();
            String codeLocalAccount = "1234";
            List<AccountSignature> dbSignatures = new ArrayList<>();
            AccountSignature dbSignature = signatureMock.createSignature();
            dbSignatures.add(dbSignature);
            when(accountSignatureRepository.findByPkCodeLocalAccount(codeLocalAccount)).thenReturn(dbSignatures);
            when(clientRequest.getClientData(dbSignature.getPk().getIdentificationType(), dbSignature.getPk().getIdentification())).thenReturn(null);
            
            RSRuntimeException exception = assertThrows(RSRuntimeException.class, () -> {
                signatureService.findSignaturesByCode(codeLocalAccount);
            });
            
            assertEquals(Messages.CLIENT_NOT_FOUND, exception.getMessage());
        }
}
