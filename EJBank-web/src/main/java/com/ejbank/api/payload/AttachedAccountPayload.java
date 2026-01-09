package com.ejbank.api.payload;

import java.math.BigDecimal;

/**
 * Représente un compte individuel dans la liste des comptes rattachés
 */
public class AttachedAccountPayload {
    private final int id;
    private final String user;
    private final String type;
    private final BigDecimal amount;
    private final long validation;

    public AttachedAccountPayload(int id, String user, String type, BigDecimal amount, long validation) {
        this.id = id;
        this.user = user;
        this.type = type;
        this.amount = amount;
        this.validation = validation;
    }

    public int getId() { return id; }
    public String getUser() { return user; }
    public String getType() { return type; }
    public BigDecimal getAmount() { return amount; }
    public long getValidation() { return validation; }
}