package com.banquito.account.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RSSignature implements Serializable {

    private String identificationType;

    private String identification;

    private String name;

    private String role;

    private String status;

    private String signature;
}
