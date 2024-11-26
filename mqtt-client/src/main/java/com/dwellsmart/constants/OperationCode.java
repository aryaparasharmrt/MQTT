package com.dwellsmart.constants;

public enum OperationCode {
    CONNECT,
    DISCONNECT,
    READ,
    W_LOAD,
    W_PP,
    W_ID;

    // Optional: To get a description or any other additional info for each enum
    @Override
    public String toString() {
        switch (this) {
            case CONNECT: return "CONNECT operation";
            case DISCONNECT: return "DISCONNECT operation";
            case READ: return "READ operation";
            case W_LOAD: return "Write Load operation";
            case W_PP: return "Write PP operation";  /* PP : Password Protected  */
            case W_ID: return "Write ID operation";
            default: return super.toString();
        }
    }
}

