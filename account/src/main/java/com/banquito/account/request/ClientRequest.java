package com.banquito.account.request;

import com.banquito.account.request.dto.RSClient;
import com.banquito.account.request.dto.RSGeneric;
import com.banquito.account.request.dto.RSTransaction;
import com.banquito.account.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ClientRequest {

    private static RestTemplate restTemplate = new RestTemplate();
    private static final String client = "http://localhost:9003/api/client";

    public static RSClient getClientData(String identificationType, String idCliente){

        String url = client.concat("/{idCliente}");

        ResponseEntity<RSClient> response = restTemplate.getForEntity(
                url,
                RSClient.class,
                idCliente
        );

        System.out.println(response);

        if(response.getStatusCode().is2xxSuccessful()){
                Utils.saveLog(response,idCliente);
                return new ObjectMapper().convertValue(response.getBody(), RSClient.class);
        }
        return null;
    }
}
