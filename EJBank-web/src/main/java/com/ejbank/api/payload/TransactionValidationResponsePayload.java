package com.ejbank.api.payload;

public class TransactionValidationResponsePayload {
    private final boolean result;
    private final String message;
    private final String error;

    public TransactionValidationResponsePayload(boolean result, String message, String error) {
        this.result = result;
        this.message = message;
        this.error = error;
    }

    public boolean isResult() { return result; }
    public String getMessage() { return message; }
    public String getError() { return error; }
}