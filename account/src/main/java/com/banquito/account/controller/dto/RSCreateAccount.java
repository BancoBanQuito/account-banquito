package com.banquito.account.controller.dto;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RSCreateAccount implements Serializable {
    private String codeLocalAccount;

    private String codeInternationalAccount;
}
