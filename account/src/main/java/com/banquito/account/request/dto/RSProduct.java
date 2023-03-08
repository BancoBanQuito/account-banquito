package com.banquito.account.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RSProduct implements Serializable {
    private String id;
    private String name;
    private String productType;
    private BigDecimal interest;
    private String capitalization;
    private Integer baseCalc;
}
