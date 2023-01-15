package com.banquito.account.controller.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RQCreateAccountSignature implements Serializable{
    private String role;
}
