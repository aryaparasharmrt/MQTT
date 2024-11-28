package com.dwellsmart.constants;

import lombok.Getter;

public enum OperationType {
    CONNECT("Connect operation"),
    DISCONNECT("Disconnect operation"),
    READ("Read operation"),
    W_LOAD("Write Load operation"), // W_LOAD: Set Load
    W_PP("Write Password Protected operation"), // W_PP: Set Protection Mode
    W_ID("Write Unit ID operation"),
    FACTORY_RESET("Factory Reset operation"),
    PASS_RESET("Reset Default Password operation");

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


