package com.ejbank.api.payload;

import java.math.BigDecimal;

public class AccountPayload {
    private int id;
    private String type;
    private BigDecimal amount;

    public AccountPayload(int id, String type, BigDecimal amount) {
        this.id = id;
        this.type = type;
        this.amount = amount;
    }

    public int getId() { return id; }
    public String getType() { return type; }
    public BigDecimal getAmount() { return amount; }
}