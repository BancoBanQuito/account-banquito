package com.banquito.account.utils;

public enum Status {

    ACTIVATE("ACT", "ACTIVO"),
    BLOCKED("BLO", "BLOQUEADO"),
    SUSPEND("SUS", "SUSPENDIDO"),
    INACTIVE("INA", "INACTIVO");

    public final String code;
    public final String name;

    Status(String code, String name) {
        this.code = code;
        this.name = name;
    }

}
