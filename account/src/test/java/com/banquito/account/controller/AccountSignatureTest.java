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
import com.banquito.account.controller.dto.RQSignatureRoleStatus;
import com.banquito.account.controller.dto.RSSignature;
import com.banquito.account.controller.mapper.AccountSignatureMapper;
import com.banquito.account.service.AccountSignatureService;
import com.banquito.account.utils.RSFormat;
import com.fasterxml.jackson.databind.ObjectMapper;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AccountSignatureTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private AccountSignatureService accSignatureService;

        @Autowired
        private ObjectMapper objectMapper;

        @Test
        @DisplayName("returns list of signatures by given an id")
        public void givenId_whenGetSignatureListById_thenReturnListRssignature() throws Exception {
                String identificationType = "C";
                String identification = "123456789";
                List<RSSignature> rsSignatures = new ArrayList<>();

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

                given(this.accSignatureService.findSignaturesById(identificationType, identification))
                                .willReturn(rsSignatures);

                ResultActions response = this.mockMvc
                                .perform(get("/api/account/signature/id/{identificationType}/{identification}",
                                                identificationType, identification));

                response.andExpect(status().isCreated())
                                .andDo(print())
                                .andExpect(jsonPath("$.size()", is(rsSignatures.size())));
        }

        @Test
        @DisplayName("returns list of signatures by given a code")
        public void givenCode_whenGetSignatureListByCode_thenReturnListRssignature() throws Exception {
                String codeLocalAccount = "123456789";
                List<RSSignature> rsSignatures = new ArrayList<>();

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

                given(this.accSignatureService.findSignaturesByCode(codeLocalAccount)).willReturn(rsSignatures);

                ResultActions response = this.mockMvc
                                .perform(get("/api/account/signature/code/{codeLocalAccount", codeLocalAccount));

                response.andExpect(status().isCreated())
                                .andDo(print())
                                .andExpect(jsonPath("$.size()", is(rsSignatures.size())));
        }
}
