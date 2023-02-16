package com.banquito.account;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import com.banquito.account.mock.AccountSignatureMock;
import com.banquito.account.model.AccountSignature;
import com.banquito.account.repository.AccountSignatureRepository;
import com.banquito.account.service.AccountSignatureService;

@SpringBootTest
public class AccountSignatureTests {

    @Mock
    private AccountSignatureRepository accSignatureRepository;

    @InjectMocks
	private AccountSignatureService accSignatureService;

    //poner signature mock
    private final AccountSignatureMock accSignatureMock = new AccountSignatureMock();

    public void givenAccountSignature_whenCreateSignature_thenSuccess() throws Exception {
        //given
        AccountSignature accSignature = accSignatureMock.createSignature();
        
        //when 
        
        //then
    }

    
}
