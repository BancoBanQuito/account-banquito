package com.banquito.account.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RQSignature implements Serializable {

    private String codeLocalAccount;

    private String codeInternationalAccount;

    private String identificationType;

    private String identification;

    private String role;

    private Date startDate;
}
