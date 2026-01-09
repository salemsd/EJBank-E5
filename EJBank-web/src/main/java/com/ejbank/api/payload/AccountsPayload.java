package com.ejbank.api.payload;

import java.util.List;

public class AccountsPayload {
    private List<AccountPayload> accounts;
    private String error;

    public AccountsPayload(List<AccountPayload> accounts) {
        this.accounts = accounts;
        this.error = null;
    }

    public List<AccountPayload> getAccounts() { return accounts; }
    public String getError() { return error; }
}