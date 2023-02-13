package com.banquito.account.utils;

import java.io.Serializable;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RSFormat<T> implements Serializable {
    private String message;
    private T data;
}
