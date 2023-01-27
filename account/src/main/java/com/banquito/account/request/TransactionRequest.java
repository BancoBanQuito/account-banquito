package com.banquito.account.request;

import com.banquito.account.request.dto.*;
import com.banquito.account.utils.Transaction;
import com.banquito.account.utils.Utils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class TransactionRequest {

    private final Transaction transactionProperties;
    private static RestTemplate restTemplate = new RestTemplate();

    public TransactionRequest(Transaction transactionProperties) {
        this.transactionProperties = transactionProperties;
    }

    public RSTransaction createTransaction(RQTransaction rqTransaction){

        String url = transactionProperties.getValue();

        // create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        //Request body parameters
        Map<String, Object> map = new HashMap<>();
        map.put("movement", rqTransaction.getMovement());
        map.put("type", rqTransaction.getType());
        map.put("codeLocalAccount", rqTransaction.getCodeLocalAccount());
        map.put("codeInternationalAccount", rqTransaction.getCodeInternationalAccount());
        map.put("concept", rqTransaction.getConcept());
        map.put("description", rqTransaction.getDescription());
        map.put("value", rqTransaction.getValue());
        map.put("recipientAccountNumber", "");
        map.put("recipientBank", "");
        map.put("recipientType", "");

        // build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        // send POST request
        ResponseEntity<RSGeneric> response = restTemplate.postForEntity(url, entity, RSGeneric.class);

        if(response.getStatusCode().is2xxSuccessful()){
            if(Utils.hasAllAttributes(response.getBody())){
                return new ObjectMapper().convertValue(response.getBody().getData(), RSTransaction.class);
            }else {
                Utils.saveLog(response, rqTransaction.getCodeLocalAccount());
            }
        }

        return null;
    }

    public List<RSTransaction> getTransactionsBetweenDates(String codeLocalAccount, LocalDateTime from, LocalDateTime to){

        String url = transactionProperties.getValue().concat("/{codeLocalAccount}/{from}/{to}");

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
            }else {
                Utils.saveLog(response, codeLocalAccount);
            }
        }

        return Collections.emptyList();
    }

    public RSInterest createSavingsAccountInterest(RQInterest rqInterest){

        String url = transactionProperties.getValue().concat("/interest");

        // create headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        //Request body parameters
        Map<String, Object> map = new HashMap<>();
        map.put("codeLocalAccount", rqInterest.getCodeLocalAccount());
        map.put("codeInternationalAccount", rqInterest.getCodeInternationalAccount());
        map.put("ear", rqInterest.getEar());
        map.put("availableBalance", rqInterest.getAvailableBalance());

        // build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        // send POST request
        ResponseEntity<RSGeneric> response = restTemplate.postForEntity(url, entity, RSGeneric.class);

        if(response.getStatusCode().is2xxSuccessful()){
            if(Utils.hasAllAttributes(response.getBody())){
                return new ObjectMapper().convertValue(response.getBody().getData(), RSInterest.class);
            }else {
                Utils.saveLog(response, rqInterest.getCodeLocalAccount());
            }
        }

        return null;
    }

    public List<RSInterest> getInterestBetweenDates(String codeLocalAccount, LocalDateTime from, LocalDateTime to){

        String url = transactionProperties.getValue().concat("/interest/{codeLocalAccount}/{from}/{to}");

        ResponseEntity<RSGeneric> response = restTemplate.getForEntity(
                url,
                RSGeneric.class,
                codeLocalAccount,
                from,
                to
        );

        if(response.getStatusCode().is2xxSuccessful()){
            if(Utils.hasAllAttributes(response.getBody())){
                RSInterest[] responses =  new ObjectMapper().convertValue(response.getBody().getData(), RSInterest[].class);
                return Arrays.asList(responses);
            }else {
                Utils.saveLog(response, codeLocalAccount);
            }
        }

        return Collections.emptyList();
    }

    public RSInvestment getInvestmentInterest(String codeLocalAccount, Integer days, BigDecimal capital, BigDecimal aer){

        String url = transactionProperties.getValue().concat("/interest/investment/{codeLocalAccount}/{days}/{capital}/{ear}");

        ResponseEntity<RSGeneric> response = restTemplate.getForEntity(
                url,
                RSGeneric.class,
                codeLocalAccount,
                days,
                capital,
                aer
        );

        if(response.getStatusCode().is2xxSuccessful()){
            if(Utils.hasAllAttributes(response.getBody())){
                return new ObjectMapper().convertValue(response.getBody().getData(), RSInvestment.class);
            }else {
                Utils.saveLog(response, codeLocalAccount);
            }
        }

        return null;
    }
}
