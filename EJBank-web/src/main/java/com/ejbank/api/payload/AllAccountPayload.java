package com.ejbank.api.payload;

import java.math.BigDecimal;

/**
 * Représente un compte dans la vue globale.
 */
public class AllAccountPayload {
    private final int id;
    private final String user;
    private final String type;
    private final BigDecimal amount;

    public AllAccountPayload(int id, String user, String type, BigDecimal amount) {
        this.id = id;
        this.user = user;
        this.type = type;
        this.amount = amount;
    }

    public int getId() { return id; }
    public String getUser() { return user; }
    public String getType() { return type; }
    public BigDecimal getAmount() { return amount; }
}