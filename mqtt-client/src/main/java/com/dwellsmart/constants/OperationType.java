package com.dwellsmart.constants;

import lombok.Getter;

public enum OperationType {
    CONNECT("Connect Operation"),
    DISCONNECT("Disconnect Operation"),
    READ("Read Operation"),
    W_LOAD("Write Load Operation"), // W_LOAD: Set Load
    W_PP("Write Password Protected Operation"), // W_PP: Set Protection Mode
    W_ID("Write Unit ID Operation"),
    FACTORY_RESET("Factory Reset Operation"),
    PASS_RESET("Reset Default Password Operation");

	@Getter
    private final String description;

    OperationType(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return description;
    }
}


