package com.banquito.account.request.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RSClientSignature implements Serializable{
    
    String typeIndentification;

    String identification;

    String name;

    String lastName;

    String signature;
}
