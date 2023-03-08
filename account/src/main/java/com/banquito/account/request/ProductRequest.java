package com.banquito.account.request;

import com.banquito.account.request.dto.RSProduct;
import com.banquito.account.utils.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class ProductRequest {

    private final Product productProperties;

    private static RestTemplate restTemplate = new RestTemplate();

    public ProductRequest(Product productProperties) { this.productProperties = productProperties; }

    public List<RSProduct> getProducts(){

        String url = productProperties.getValue();

        ResponseEntity<Object> response = restTemplate.getForEntity(
                url,
                Object.class
        );

        if(response.getStatusCode().is2xxSuccessful()){

            RSProduct[] responses =  new ObjectMapper().convertValue(response.getBody(), RSProduct[].class);
            return Arrays.asList(responses);
        }

        return Collections.emptyList();
    }
}
