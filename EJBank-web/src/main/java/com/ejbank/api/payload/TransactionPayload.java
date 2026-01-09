package com.ejbank.api.payload;

import java.math.BigDecimal;

public class TransactionPayload {
    private final int id;
    private final String date;
    private final String source;
    private final String destination;
    private final String destination_user;
    private final String source_user;
    private final BigDecimal amount;
    private final String author;
    private final String comment;
    private final String state;

    public TransactionPayload(int id, String date, String source, String destination,
                              String destination_user, String source_user, BigDecimal amount,
                              String author, String comment, String state) {
        this.id = id;
        this.date = date;
        this.source = source;
        this.destination = destination;
        this.destination_user = destination_user;
        this.source_user = source_user;
        this.amount = amount;
        this.author = author;
        this.comment = comment;
        this.state = state;
    }

    public int getId() { return id; }
    public String getDate() { return date; }
    public String getSource() { return source; }
    public String getDestination() { return destination; }
    public String getDestination_user() { return destination_user; }
    public String getSource_user() { return source_user; }
    public BigDecimal getAmount() { return amount; }
    public String getAuthor() { return author; }
    public String getComment() { return comment; }
    public String getState() { return state; }
}