package com.banquito.account.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RQAccountAssociatedService {

    private String codeLocalAccount;

    private String codeProductType;

    private String codeProduct;

    private String codeAssociatedService;
}
