package com.banquito.account;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.banquito.account.controller.dto.RSSignature;
import com.banquito.account.mock.AccountSignatureMock;
import com.banquito.account.model.Account;
import com.banquito.account.model.AccountSignature;
import com.banquito.account.repository.AccountRepository;
import com.banquito.account.repository.AccountSignatureRepository;
import com.banquito.account.request.ClientRequest;
import com.banquito.account.request.dto.RSClientSignature;
import com.banquito.account.service.AccountSignatureService;

@SpringBootTest
public class AccountSignatureTests {

    @Mock
    private AccountSignatureRepository accSignatureRepository;

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private ClientRequest clientRequest;

    @InjectMocks
	private AccountSignatureService accSignatureService;


    @Test
    public void givenAccountSignature_whenCreateSignature_thenSuccess() throws Exception {
        //given
        AccountSignature accSignatureExpected = AccountSignatureMock.createSignature();
        AccountSignature accSignatureActual = AccountSignatureMock.createSignature();
        Account account = AccountSignatureMock.createAccount();
        RSClientSignature clientSignature = AccountSignatureMock.createRSClientSignature();
        ArgumentCaptor<AccountSignature> accSignatureCaptor = ArgumentCaptor.forClass(AccountSignature.class);

        //when 
        when(clientRequest.getClientData(any(String.class), any(String.class)))
                .thenReturn(clientSignature);

        when(accountRepository.findByPkCodeLocalAccount(any(String.class)))
                .thenReturn(Optional.of(account));

        this.accSignatureService.createSignature(accSignatureExpected);

        //then
        verify(accSignatureRepository).save(accSignatureCaptor.capture());
        assertEquals(accSignatureExpected, accSignatureCaptor.getValue());
    }

    public void givenData_whenUpdateSignatureRoleStatus_thenSuccess() throws Exception {
        //given
        AccountSignature accSignature = AccountSignatureMock.createSignature();
        String identificationType = "CED";
        String identification = "12345678901234567890";
        String codeLocalAccount = "12345678901234567890";
        String role = "Owner";
        String status = "ACT";
        ArgumentCaptor<AccountSignature> accSignatureCaptor = ArgumentCaptor.forClass(AccountSignature.class);

        //when
        when(accSignatureRepository.findByPkIdentificationTypeAndPkIdentificationAndPkCodeLocalAccount(identificationType, identification, codeLocalAccount))
                .thenReturn(Optional.of(accSignature));

        this.accSignatureService.updateSignatureRoleStatus(identificationType, identification, codeLocalAccount, role, status);

        //then
        verify(accSignatureRepository).save(accSignatureCaptor.capture());
        assertEquals(accSignature, accSignatureCaptor.getValue());
    }

    @Test
    public void givenData_whenFindSginatureByID_thenReturnListOfRSSignature() throws Exception{
        //  Given
        String identificationType = "CED";
        String identification = "12345678901234567890";
        List <RSSignature> rSSignatueExpected = AccountSignatureMock.listCreateRSSignature();
        List <AccountSignature> accSignature = AccountSignatureMock.listCreateSignature();
        RSClientSignature clientSignature = AccountSignatureMock.createRSClientSignature();

        // When
        when(accSignatureRepository.findByPkIdentificationTypeAndPkIdentification(any(String.class), any(String.class)))
                .thenReturn(accSignature);

        when(clientRequest.getClientData(any(String.class), any(String.class)))
                .thenReturn(clientSignature);
        
        // Action
        List <RSSignature> rSSignatueActual = accSignatureService.findSignaturesById(identificationType, identification);

        // Assert
        assertEquals(rSSignatueExpected, rSSignatueActual);

    }

    
}
