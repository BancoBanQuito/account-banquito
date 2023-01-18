package com.banquito.account.controller.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class RQSignatureRoleStatus implements Serializable {
    private String role;
    private String status;
}
