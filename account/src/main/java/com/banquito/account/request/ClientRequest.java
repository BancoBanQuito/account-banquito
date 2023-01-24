package com.banquito.account.request;

import com.banquito.account.request.dto.RSClientSignature;
import com.banquito.account.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class ClientRequest {

    private static RestTemplate restTemplate = new RestTemplate();
    private static final String client = "http://localhost:9003/api/client";

    //private static final String client = "/api/client";
    public static RSClientSignature getClientData(String typeIdentification, String identification){

        String url = client.concat("/signature/{typeIdentification}/{identification}");

        ResponseEntity<RSClientSignature> response = restTemplate.getForEntity(
                url,
                RSClientSignature.class,
                typeIdentification,
                identification
        );

        if(response.getStatusCode().is2xxSuccessful()){
                Utils.saveLog(response,identification);
                return new ObjectMapper().convertValue(response.getBody(), RSClientSignature.class);
        }
        return null;
    }
}
