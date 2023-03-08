package com.banquito.account.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "spring.variable.product")
@Configuration("productProperties")
public class Product {

    private String value;
}
