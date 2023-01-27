package com.banquito.account.request;

import com.banquito.account.request.dto.RSClientSignature;
import com.banquito.account.utils.Client;
import com.banquito.account.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ClientRequest {

    private final Client clientProperties;

    private static RestTemplate restTemplate = new RestTemplate();

    public ClientRequest(Client clientProperties) {
        this.clientProperties = clientProperties;
    }

    public RSClientSignature getClientData(String typeIdentification, String identification) {

        String url = clientProperties.getValue().concat("/signature/{typeIdentification}/{identification}");

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
