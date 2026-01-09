package com.ejbank.api.payload;

import java.math.BigDecimal;

public class AccountDetailResponse {
    private final String owner;
    private final String advisor;
    private final BigDecimal rate;
    private final BigDecimal interest;
    private final BigDecimal amount;
    private final String error;

    public AccountDetailResponse(String owner, String advisor, BigDecimal rate, BigDecimal interest, BigDecimal amount, String error) {
        this.owner = owner;
        this.advisor = advisor;
        this.rate = rate;
        this.interest = interest;
        this.amount = amount;
        this.error = error;
    }

    public String getOwner() { return owner; }
    public String getAdvisor() { return advisor; }
    public BigDecimal getRate() { return rate; }
    public BigDecimal getInterest() { return interest; }
    public BigDecimal getAmount() { return amount; }
    public String getError() { return error; }
}