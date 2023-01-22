package com.banquito.account.utils;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class Product {
    private String code;
    private String type;
    private BigDecimal interest;
}
