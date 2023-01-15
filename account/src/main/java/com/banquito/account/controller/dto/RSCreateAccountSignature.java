package com.banquito.account.controller.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RSCreateAccountSignature implements Serializable{
    private String identificationtype;
    private String identification;
    private String name;
    private String role;
    private String status;
    private String signature;
}
