package com.ejbank.api.payload;

public class TransactionValidationRequestPayload {
    private int transaction;
    private boolean approve;
    private int author;

    public TransactionValidationRequestPayload() {}

    /**
     * @return l'identifiant de la transaction à traiter
     */
    public int getTransaction() { return transaction; }
    public void setTransaction(int transaction) { this.transaction = transaction; }

    /**
     * @return true pour approuver, false pour refuser
     */
    public boolean isApprove() { return approve; }
    public void setApprove(boolean approve) { this.approve = approve; }

    /**
     * @return l'identifiant du conseiller effectuant l'action
     */
    public int getAuthor() { return author; }
    public void setAuthor(int author) { this.author = author; }
}