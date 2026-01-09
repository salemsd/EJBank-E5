package com.ejbank.api.payload;

import java.util.List;

public class AttachedAccountsResponse {
    private final List<AttachedAccountPayload> accounts;
    private final String error;

    /**
     * Constructeur pour une réponse réussie.
     * @param accounts la liste des comptes rattachés
     */
    public AttachedAccountsResponse(List<AttachedAccountPayload> accounts) {
        this.accounts = accounts;
        this.error = null;
    }

    /**
     * Constructeur pour une réponse en cas d'erreur.
     * @param error le message d'erreur
     */
    public AttachedAccountsResponse(String error) {
        this.accounts = null;
        this.error = error;
    }

    /**
     * @return la liste des comptes (mappée sur la clé "accounts" en JSON)
     */
    public List<AttachedAccountPayload> getAccounts() {
        return accounts;
    }

    /**
     * @return le message d'erreur éventuel
     */
    public String getError() {
        return error;
    }
}