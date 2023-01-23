package com.banquito.account.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RSAccountAssociatedService {

    private String codeLocalAccount;

    private String codeInternationalAccount;

    private String codeAssociatedService;

    private String status;

    private Date startDate;
}
