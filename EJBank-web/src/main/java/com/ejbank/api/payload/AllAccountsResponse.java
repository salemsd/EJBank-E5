package com.ejbank.api.payload;

import java.util.List;

/**
 * Payload de réponse pour la vue globale des comptes.
 */
public class AllAccountsResponse {
    private final List<AllAccountPayload> accounts;
    private final String error;

    public AllAccountsResponse(List<AllAccountPayload> accounts) {
        this.accounts = accounts;
        this.error = null;
    }

    public List<AllAccountPayload> getAccounts() { return accounts; }
    public String getError() { return error; }
}