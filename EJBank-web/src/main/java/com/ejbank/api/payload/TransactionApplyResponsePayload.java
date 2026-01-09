package com.ejbank.api.payload;

public class TransactionApplyResponsePayload {
    private final boolean result;
    private final String message;

    public TransactionApplyResponsePayload(boolean result, String message) {
        this.result = result;
        this.message = message;
    }

    /**
     * @return true si l'opération a réussi (ou est en attente), false sinon
     */
    public boolean isResult() { return result; }

    /**
     * @return le message de succès ou d'erreur renvoyé par le serveur
     */
    public String getMessage() { return message; }
}