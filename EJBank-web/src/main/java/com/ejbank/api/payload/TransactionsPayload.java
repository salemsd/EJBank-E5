package com.ejbank.api.payload;

import java.util.ArrayList;
import java.util.List;

public class TransactionsPayload {
    private long total;
    private List<TransactionPayload> transactions;
    private String error;

    public TransactionsPayload(long total, List<TransactionPayload> transactions) {
        this.total = total;
        this.transactions = transactions;
        this.error = null;
    }

    public TransactionsPayload(String error) {
        this.total = 0;
        this.transactions = new ArrayList<>();
        this.error = error;
    }

    public long getTotal() { return total; }
    public List<TransactionPayload> getTransactions() { return transactions; }
    public String getError() { return error; }
}