package com.ejbank.api;

import com.ejbank.api.payload.AccountDetailPayload;
import com.ejbank.beans.AccountBean;
import com.ejbank.entities.Account;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.math.BigDecimal;

@Path("/account")
public class AccountResource {

    @EJB
    private AccountBean accountBean;

    /**
     * Endpoint : GET /account/{account_id}/{user_id}
     * Fournit les détails du compte ou un signal de redirection si l'accès est invalide.
     */
    @GET
    @Path("/{account_id}/{user_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccountDetail(@PathParam("account_id") int accountId, @PathParam("user_id") int userId) {
        Account account = accountBean.getAccountById(accountId);

        boolean isOwner = (account != null && account.getCustomer().getId() == userId);
        boolean isAdvisor = (account != null && account.getCustomer().getAdvisor() != null &&
                account.getCustomer().getAdvisor().getId() == userId);

        if (account == null || (!isOwner && !isAdvisor)) {
            return Response.ok(new AccountDetailPayload("Vous n'êtes pas autorisé à voir un autre compte")).build();
        }

        AccountDetailPayload response = getAccountDetailPayload(account);

        return Response.ok(response).build();
    }

    private static AccountDetailPayload getAccountDetailPayload(Account account) {
        BigDecimal balance = account.getBalance();
        BigDecimal rate = account.getAccountType().getRate();
        BigDecimal interest = balance.multiply(rate).divide(new BigDecimal(100));

        String ownerName = account.getCustomer().getFirstname() + " " + account.getCustomer().getLastname();
        String advisorName = account.getCustomer().getAdvisor() != null ?
                account.getCustomer().getAdvisor().getFirstname() + " " + account.getCustomer().getAdvisor().getLastname() :
                "Aucun conseiller";

        return new AccountDetailPayload(
                ownerName,
                advisorName,
                rate,
                interest,
                balance
        );
    }
}