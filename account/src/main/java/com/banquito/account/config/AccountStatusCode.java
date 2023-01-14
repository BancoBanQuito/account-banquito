package com.banquito.account.config;

public enum AccountStatusCode {

    ACTIVATE("ACT", "ACTIVO"),
    BLOCKED("BLO", "BLOQUEADA"),
    SUSPEND("SUS", "SUSPENDIDO");

    public final String code;
    public final String name;

    AccountStatusCode(String code, String name) {
        this.code = code;
        this.name = name;
    }

}
