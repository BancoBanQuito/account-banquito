package com.banquito.account.controller.dto;

import com.banquito.account.request.dto.RSInterest;
import com.banquito.account.request.dto.RSTransaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RSComputeInterest {

    RSInterest interest;

    RSTransaction transaction;
}
