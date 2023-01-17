package com.banquito.account.utils;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseFormat implements Serializable {
    private String message;
    private Object data;
}
