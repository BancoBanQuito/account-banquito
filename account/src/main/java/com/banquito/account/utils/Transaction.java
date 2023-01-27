package com.banquito.account.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "spring.variable.transaction")
@Configuration("transactionProperties")
public class Transaction {

    private String value;
}
