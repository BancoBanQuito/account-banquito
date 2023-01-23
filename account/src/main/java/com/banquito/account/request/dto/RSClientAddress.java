package com.banquito.account.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RSClientAddress {

    private String codeLocation;
    private String lineOne;
    private String lineTwo;
    private String latitude;
    private String longitude;
}
