package com.ejbank.beans.models;

import java.math.BigDecimal;

/**
 * Représente le résultat interne d'une opération de transaction ou de simulation.
 */
public class TransactionResponse {
    private final boolean success;
    private final BigDecimal balanceBefore;
    private final BigDecimal balanceAfter;
    private final String errorMessage;

    public TransactionResponse(boolean success, BigDecimal balanceBefore, BigDecimal balanceAfter, String errorMessage) {
        this.success = success;
        this.balanceBefore = balanceBefore;
        this.balanceAfter = balanceAfter;
        this.errorMessage = errorMessage;
    }

    public boolean isSuccess() { return success; }
    public BigDecimal getBalanceBefore() { return balanceBefore; }
    public BigDecimal getBalanceAfter() { return balanceAfter; }
    public String getErrorMessage() { return errorMessage; }
}