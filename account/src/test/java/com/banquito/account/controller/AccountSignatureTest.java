package com.banquito.account.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.banquito.account.controller.dto.RQSignature;
import com.banquito.account.controller.dto.RSSignature;
import com.banquito.account.controller.mapper.AccountSignatureMapper;
import com.banquito.account.service.AccountSignatureService;
import com.banquito.account.utils.RSFormat;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountSignatureTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountSignatureService accSignatureService;

    @Autowired
    private ObjectMapper objectMapper;

    /*@Test
    @DisplayName("returns OK by given an mapped account signature")
    public void givenAccountSignature_whenCreateSignature_thenSuccess() throws Exception{
        //given (RQsignature) return RSFormat
        RQSignature rqSignature = new RQSignature();
        RSFormat <String> rsFormat;
        //AccountSignatureMapper accountSignatureMapper = new AccountSignatureMapper();
        rsFormat.<String>builder().message("Success").data("Created").build();

        rqSignature.builder()
                .codeLocalAccount("123456789")
                .identificationType("C")
                .identification("123456789")
                .role("A")
                .startDate(new Date())
                .build();   
                
        willDoNothing().given(accSignatureService).createSignature(AccountSignatureMapper.map(rqSignature));

        ResultActions response = this.mockMvc.perform(post("/api/account/signature")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rqSignature)));
    }*/

    @Test
    @DisplayName("returns list of signatures by given an id")
    public void givenId_whenGetSignatureList_thenReturnListRssignature() throws Exception{
        //given (RQsignature) return RSFormat
        String identificationType = "C";
        String identification = "123456789";
        List<RSSignature> rsSignatures = new ArrayList<>();

       /*  RQSignature rqSignature = new RQSignature();
        RSFormat <String> rsFormat;
        //AccountSignatureMapper accountSignatureMapper = new AccountSignatureMapper();
        rsFormat.<String>builder().message("Success").data("Created").build();*/

        rsSignatures.add(RSSignature.builder()
                .identificationType("C")
                .identification("123456789")
                .name("Madely Betancourt")
                .role("A")
                .status("ACTIVE")
                .signature("abc")
                .build());
        
        rsSignatures.add(RSSignature.builder()
                .identificationType("R")
                .identification("123456789001")
                .name("Melani Carillo")
                .role("A")
                .status("ACTIVE")
                .signature("abc")
                .build());
       
    }
    
}
