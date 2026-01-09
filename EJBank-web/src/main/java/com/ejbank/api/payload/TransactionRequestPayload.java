package com.ejbank.api.payload;

import java.math.BigDecimal;

public class TransactionRequestPayload {
    private int source;
    private int destination;
    private BigDecimal amount;
    private String comment;
    private int author;

    public TransactionRequestPayload() {}

    /**
     * @return l'identifiant du compte source
     */
    public int getSource() { return source; }
    public void setSource(int source) { this.source = source; }

    /**
     * @return l'identifiant du compte destination
     */
    public int getDestination() { return destination; }
    public void setDestination(int destination) { this.destination = destination; }

    /**
     * @return le montant de la transaction
     */
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    /**
     * @return le libellé de l'opération
     */
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    /**
     * @return l'identifiant de l'auteur de l'action
     */
    public int getAuthor() { return author; }
    public void setAuthor(int author) { this.author = author; }
}