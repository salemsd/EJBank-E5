package com.ejbank.api;

import com.ejbank.api.payload.*;
import com.ejbank.beans.TransactionBean;
import com.ejbank.beans.models.TransactionResponse;
import com.ejbank.entities.Account;
import com.ejbank.entities.Transaction;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Path("/transaction")
public class TransactionResource {

    @EJB
    private TransactionBean transactionBean;

    private static final int LIMIT = 10;

    /**
     * Récupère la liste paginée des transactions pour un compte spécifique.
     * Applique les règles de sécurité : accès restreint au propriétaire du compte ou à son conseiller.
     *
     * @param accountId l'identifiant du compte concerné
     * @param offset l'index de départ pour la pagination
     * @param userId l'ID de l'utilisateur demandeur (pour vérification des droits)
     * @return Response contenant un TransactionsPayload (liste + total) ou une erreur 403/404
     */
    @GET
    @Path("/list/{account_id}/{offset}/{user_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listTransactions(@PathParam("account_id") int accountId,
                                     @PathParam("offset") int offset,
                                     @PathParam("user_id") int userId) {

        Account account = transactionBean.getAccountById(accountId);

        boolean isOwner = (account != null && account.getCustomer().getId() == userId);
        boolean isAdvisor = (account != null && account.getCustomer().getAdvisor() != null &&
                account.getCustomer().getAdvisor().getId() == userId);

        if (account == null || (!isOwner && !isAdvisor)) {
            return Response.ok(new TransactionsPayload("Vous n'êtes pas autorisé à voir les transactions d'un autre compte")).build();
        }

        long total = transactionBean.getAllTransactionsCountByAccountId(accountId);
        List<Transaction> transactions = transactionBean.getAllTransactionsByAccountId(accountId, offset, LIMIT);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<TransactionPayload> transactionList = transactions.stream().map(t -> {
            String state;
            if (t.isApplied()) {
                state = "APPLYED";
            } else {
                if (isAdvisor) {
                    state = "TO_APPROVE";
                } else {
                    state = "WAITING_APPROVE";
                }
            }

            String sourceLabel = t.getAccountFrom().getAccountType().getName();
            String destLabel = t.getAccountTo().getAccountType().getName();

            String sourceUser = t.getAccountFrom().getCustomer().getFirstname();
            String destUser = t.getAccountTo().getCustomer().getFirstname();

            String authorName = t.getAuthor().getFirstname() + " " + t.getAuthor().getLastname();

            return new TransactionPayload(
                    t.getId(),
                    t.getDate().format(formatter),
                    sourceLabel,
                    destLabel,
                    destUser,
                    sourceUser,
                    t.getAmount(),
                    authorName,
                    t.getComment(),
                    state
            );
        }).collect(Collectors.toList());

        return Response.ok(new TransactionsPayload(total, transactionList)).build();
    }

    /**
     * Endpoint pour simuler un virement.
     * @param request payload contenant source, destination, montant et auteur
     */
    @POST
    @Path("/preview")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response previewTransaction(TransactionRequestPayload request) {
        TransactionResponse res = transactionBean.getTransactionPreview(
                request.getSource(),
                request.getDestination(),
                request.getAmount(),
                request.getAuthor()
        );

        TransactionPreviewPayload payload = new TransactionPreviewPayload(
                res.isSuccess(),
                res.getBalanceBefore(),
                res.getBalanceAfter(),
                res.isSuccess() ? res.getErrorMessage() : null,
                res.isSuccess() ? null : res.getErrorMessage()
        );

        return Response.ok(payload).build();
    }

    /**
     * Endpoint pour appliquer un virement.
     *
     * @param request le payload de la transaction
     * @return Response HTTP 200 avec le résultat JSON
     */
    @POST
    @Path("/apply")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response applyTransaction(TransactionRequestPayload request) {
        TransactionResponse res = transactionBean.applyTransaction(
                request.getSource(),
                request.getDestination(),
                request.getAmount(),
                request.getComment(),
                request.getAuthor()
        );

        TransactionApplyResponsePayload response = new TransactionApplyResponsePayload(
                res.isSuccess(),
                res.isSuccess() ? res.getErrorMessage() : res.getErrorMessage()
        );

        return Response.ok(response).build();
    }

    /**
     * Endpoint pour valider ou refuser une transaction.
     * @param request payload contenant l'ID transaction, le choix et l'auteur
     */
    @POST
    @Path("/validation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateTransaction(TransactionValidationRequestPayload request) {
        TransactionResponse res = transactionBean.validateTransaction(
                request.getTransaction(),
                request.isApprove(),
                request.getAuthor()
        );

        TransactionValidationResponsePayload response = new TransactionValidationResponsePayload(
                res.isSuccess(),
                res.isSuccess() ? res.getErrorMessage() : null,
                res.isSuccess() ? null : res.getErrorMessage()
        );

        return Response.ok(response).build();
    }

    /**
     * Retourne le nombre de transactions en attente de validation pour un conseiller donné.
     *
     * @param userId l'identifiant de l'utilisateur (conseiller)
     * @return le nombre total de transactions à valider
     */
    @GET
    @Path("/validation/notification/{user_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public long getNotificationCount(@PathParam("user_id") int userId) {
        // Justification : On donjne le compteur au Bean Stateless qui utilise
        // une requête COUNT optimisée en bdd
        return transactionBean.countPendingTransactions(userId);
    }


}