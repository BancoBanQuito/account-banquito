package com.banquito.account.config;

public enum RSCode {
    INTERNAL_ERROR_SERVER(500),
    NOT_FOUND(404),
    CREATED(201),
    SUCCESS(200);

    public final int code;
    RSCode(int code) {
        this.code = code;
    }
}
