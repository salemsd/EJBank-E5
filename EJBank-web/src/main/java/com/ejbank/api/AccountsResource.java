package com.ejbank.api;

import com.ejbank.api.payload.*;
import com.ejbank.beans.UserBean;
import com.ejbank.entities.Account;

import javax.ejb.EJB;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/accounts")
public class AccountsResource {
    @EJB
    private UserBean userBean;

    /**
     * Retourne la liste des comptes rattachés à un conseiller.
     * @param userId ID du conseiller
     */
    @GET
    @Path("/attached/{user_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAttachedAccounts(@PathParam("user_id") int userId) {
        List<Account> accounts = userBean.getAttachedAccountsByAdvisorId(userId);

        List<AttachedAccountPayload> attachedPayloads = accounts.stream()
                .map(a -> {
                    long pending = a.getTransactionsFrom().stream()
                            .filter(t -> !t.isApplied())
                            .count();

                    return new AttachedAccountPayload(
                            a.getId(),
                            a.getCustomer().getFirstname(),
                            a.getAccountType().getName(),
                            a.getBalance(),
                            pending
                    );
                })
                .collect(Collectors.toList());

        return Response.ok(new com.ejbank.api.payload.AttachedAccountsResponse(attachedPayloads)).build();
    }

    /**
     * Retourne la vue globale des comptes pour un utilisateur.
     * @param userId l'ID de l'utilisateur (client ou conseiller)
     */
    @GET
    @Path("/all/{user_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAccounts(@PathParam("user_id") int userId) {
        List<Account> accounts = userBean.getAllVisibleAccounts(userId);

        List<AllAccountPayload> allPayloads = accounts.stream()
                .map(a -> new AllAccountPayload(
                        a.getId(),
                        a.getCustomer().getFirstname(),
                        a.getAccountType().getName(),
                        a.getBalance()
                ))
                .collect(Collectors.toList());

        return Response.ok(new AllAccountsResponse(allPayloads)).build();
    }

    @GET
    @Path("/{user_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserAccounts(@PathParam("user_id") int userId) {
        List<Account> accounts = userBean.getAccountsByUserId(userId);

        List<AccountPayload> dtos = accounts.stream()
                .map(acc -> new AccountPayload(
                        acc.getId(),
                        acc.getAccountType().getName(),
                        acc.getBalance()
                ))
                .collect(Collectors.toList());

        return Response.ok(new AccountsPayload(dtos)).build();
    }
}