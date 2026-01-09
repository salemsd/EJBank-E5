package com.ejbank.api.payload;

import java.math.BigDecimal;

public class TransactionPreviewPayload {
    private final boolean result;
    private final BigDecimal before;
    private final BigDecimal after;
    private final String message;
    private final String error;

    public TransactionPreviewPayload(boolean result, BigDecimal before, BigDecimal after, String message, String error) {
        this.result = result;
        this.before = before;
        this.after = after;
        this.message = message;
        this.error = error;
    }

    public boolean isResult() { return result; }
    public BigDecimal getBefore() { return before; }
    public BigDecimal getAfter() { return after; }
    public String getMessage() { return message; }
    public String getError() { return error; }
}