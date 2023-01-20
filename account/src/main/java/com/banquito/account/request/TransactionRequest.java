package com.banquito.account.request;

import com.banquito.account.request.dto.RSGeneric;
import com.banquito.account.request.dto.RSTransaction;
import com.banquito.account.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TransactionRequest {

    private static RestTemplate restTemplate = new RestTemplate();
    private static final String transaction = "http://localhost:9002/api/transaction";

    public static List<RSTransaction> getTransactions(String codeLocalAccount, String from, String to){

        String url = transaction.concat("/{codeLocalAccount}/{from}/{to}");

        ResponseEntity<RSGeneric> response = restTemplate.getForEntity(
                url,
                RSGeneric.class,
                codeLocalAccount,
                from,
                to
        );

        if(response.getStatusCode().is2xxSuccessful()){
            if(Utils.hasAllAttributes(response.getBody())){
                RSTransaction[] responses =  new ObjectMapper().convertValue(response.getBody().getData(), RSTransaction[].class);
                return Arrays.asList(responses);
            }
        }

        return Collections.emptyList();
    }

}
