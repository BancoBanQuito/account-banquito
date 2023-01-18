package com.banquito.account.utils;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignatureUpdate {
    private String codeLocalAccount;

    private String codeInternationalAccount;

    private String identificationType;

    private String identification;

    private String role;

    private String status;
}
