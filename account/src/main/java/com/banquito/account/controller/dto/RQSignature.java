package com.banquito.account.controller.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class RQSignature implements Serializable {

    private String codeLocalAccount;

    private String codeInternationalAccount;

    private String identificationType;

    private String identification;

    private String role;

    private Date startDate;
}
