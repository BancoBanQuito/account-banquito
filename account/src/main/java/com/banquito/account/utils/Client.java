package com.banquito.account.utils;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "spring.variable.client")
@Configuration("clientProperties")
public class Client {

    private String value;

}
