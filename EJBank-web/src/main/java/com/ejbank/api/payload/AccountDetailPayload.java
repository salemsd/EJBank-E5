package com.ejbank.api.payload;

import java.math.BigDecimal;


public class AccountDetailPayload {
    private String owner;
    private String advisor;
    private BigDecimal rate;
    private BigDecimal interest;
    private BigDecimal amount;
    private String error;

    public AccountDetailPayload() {}

    public AccountDetailPayload(String owner, String advisor, BigDecimal rate, BigDecimal interest, BigDecimal amount) {
        this.owner = owner;
        this.advisor = advisor;
        this.rate = rate;
        this.interest = interest;
        this.amount = amount;
        this.error = null;
    }

    public AccountDetailPayload(String error) {
        this.error = error;
    }

    public String getOwner() { return owner; }
    public String getAdvisor() { return advisor; }
    public BigDecimal getRate() { return rate; }
    public BigDecimal getInterest() { return interest; }
    public BigDecimal getAmount() { return amount; }
    public String getError() { return error; }
}