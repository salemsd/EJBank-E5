package com.ejbank.beans;

import com.ejbank.beans.models.TransactionResponse;
import com.ejbank.entities.Account;
import com.ejbank.entities.Transaction;
import com.ejbank.entities.User;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.util.List;

@Stateless
@LocalBean
public class TransactionBean {

    @PersistenceContext(unitName = "EJBankPU")
    private EntityManager em;

    /**
     * Récupère le nombre total de transactions pour un compte donné.
     * Prend en compte les transactions émettrices et réceptrices.
     *
     * @param accountId l'identifiant du compte
     * @return le nombre total de transactions
     */
    public long getAllTransactionsCountByAccountId(int accountId) {
        return em.createQuery(
                "SELECT COUNT(t) FROM Transaction t WHERE t.accountFrom.id = :accountId OR t.accountTo.id = :accountId", Long.class)
                .setParameter("accountId", accountId)
                .getSingleResult();
    }

    /**
     * Récupère la liste paginée des transactions pour un compte donné.
     * Trie les résultats par date décroissante (les plus récentes en premier).
     * Utilise JOIN FETCH pour optimiser le chargement des comptes liés et éviter le LazyLoading.
     *
     * @param accountId l'identifiant du compte
     * @param offset la position de départ (pagination)
     * @param limit le nombre maximum de résultats à retourner
     * @return une liste de transactions
     */
    public List<Transaction> getAllTransactionsByAccountId(int accountId, int offset, int limit) {
        return em.createQuery(
                        "SELECT t FROM Transaction t " +
                                "JOIN FETCH t.accountFrom af JOIN FETCH af.accountType " +
                                "JOIN FETCH af.customer " +
                                "JOIN FETCH t.accountTo ato JOIN FETCH ato.accountType " +
                                "JOIN FETCH ato.customer " +
                                "JOIN FETCH t.author " +
                                "WHERE t.accountFrom.id = :accountId OR t.accountTo.id = :accountId " +
                                "ORDER BY t.date DESC", Transaction.class)
                .setParameter("accountId", accountId)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }
    
    /**
     * Récupère un compte par son ID pour vérification d'appartenance.
     * * @param accountId l'identifiant du compte
     * @return l'entité Account ou null si inexistante
     */
    public Account getAccountById(int accountId) {
        return em.find(Account.class, accountId);
    }


    /**
     * Simule une transaction pour vérifier sa faisabilité.
     */
    public TransactionResponse getTransactionPreview(int sourceId, int destId, BigDecimal amount, int authorId) {
        Account source = em.find(Account.class, sourceId);
        Account dest = em.find(Account.class, destId);
        User author = em.find(User.class, authorId);

        if (source == null || dest == null || author == null) {
            return new TransactionResponse(false, BigDecimal.ZERO, BigDecimal.ZERO, "Données invalides : comptes ou auteur introuvables");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            return new TransactionResponse(false, source.getBalance(), source.getBalance(), "Le montant doit être supérieur à zéro");
        }

        String message = "Simulation réussie";

        if (author.requiresValidation(amount)) {
            message = "Simulation réussie (virement soumis à validation de votre conseiller)";
        }

        BigDecimal before = source.getBalance();
        BigDecimal after = before.subtract(amount);

        BigDecimal limit = new BigDecimal(source.getAccountType().getOverdraft()).negate();

        if (after.compareTo(limit) < 0) {
            return new TransactionResponse(false, before, after, "Solde insuffisant (limite de découvert dépassée)");
        }

        return new TransactionResponse(true, before, after, message);
    }

    /**
     * Compte le nombre de transactions en attente de validation pour un conseiller donné.
     * Une transaction nécessite validation si elle n'est pas appliquée et concerne un de ses clients.
     *
     * @param advisorId ID du conseiller
     * @return le nombre de transactions en attente
     */
    public long countPendingTransactions(int advisorId) {
        return em.createQuery(
                        "SELECT COUNT(t) FROM Transaction t WHERE t.applied = false AND t.accountFrom.customer.advisor.id = :advisorId", Long.class)
                .setParameter("advisorId", advisorId)
                .getSingleResult();
    }

    /**
     * Valide ou refuse une transaction en attente.
     * Seul le conseiller rattaché au client peut effectuer cette action.
     *
     * @param transactionId ID de la transaction
     * @param approve true pour valider, false pour supprimer
     * @param advisorId ID de l'auteur (doit être un conseiller)
     * @return TransactionResponse indiquant le résultat de l'opération
     */
    public TransactionResponse validateTransaction(int transactionId, boolean approve, int advisorId) {
        Transaction t = em.find(Transaction.class, transactionId);
        if (t == null) {
            return new TransactionResponse(false, null, null, "Transaction introuvable");
        }

        if (t.isApplied()) {
            return new TransactionResponse(false, null, null, "Cette transaction a déjà été traitée");
        }

        Account source = t.getAccountFrom();
        if (source == null || source.getCustomer() == null || source.getCustomer().getAdvisor() == null) {
            return new TransactionResponse(false, null, null, "Données de compte ou conseiller corrompues");
        }

        if (source.getCustomer().getAdvisor().getId() != advisorId) {
            return new TransactionResponse(false, null, null, "Action interdite : vous n'êtes pas le conseiller de ce client");
        }

        if (approve) {
            Account dest = t.getAccountTo();
            BigDecimal amount = t.getAmount();

            BigDecimal limit = new BigDecimal(source.getAccountType().getOverdraft()).negate();
            BigDecimal projectedBalance = source.getBalance().subtract(amount);

            if (projectedBalance.compareTo(limit) < 0) {
                return new TransactionResponse(false, source.getBalance(), projectedBalance, "Solde insuffisant (limite de découvert dépassée)");
            }

            source.setBalance(projectedBalance);
            dest.setBalance(dest.getBalance().add(amount));

            t.setApplied(true);
            t.setDate(java.time.LocalDateTime.now());

            return new TransactionResponse(true, source.getBalance(), projectedBalance, "La transaction a été approuvée et effectuée");
        } else {
            em.remove(t);
            return new TransactionResponse(true, null, null, "La demande de virement a été refusée et supprimée");
        }
    }

    /**
     * Exécute ou enregistre une demande de virement entre deux comptes.
     * Applique les règles de sécurité (limite de 1000€ pour les clients)
     * et la gestion des états (validation par conseiller).
     *
     * @param sourceId ID du compte à débiter
     * @param destId ID du compte à créditer
     * @param amount montant de la transaction
     * @param comment libellé de la transaction
     * @param authorId ID de l'utilisateur effectuant l'action
     * @return TransactionResponse contenant le résultat métier
     */
    public TransactionResponse applyTransaction(int sourceId, int destId, BigDecimal amount, String comment, int authorId) {
        Account source = em.find(Account.class, sourceId);
        Account dest = em.find(Account.class, destId);
        User author = em.find(User.class, authorId);

        if (source == null || dest == null || author == null) {
            return new TransactionResponse(false, null, null, "Données invalides : comptes ou auteur introuvables");
        }

        boolean applied = author.shouldAutoApply(amount);

        BigDecimal limit = new BigDecimal(source.getAccountType().getOverdraft()).negate();
        BigDecimal newSourceBalance = source.getBalance().subtract(amount);

        if (newSourceBalance.compareTo(limit) < 0) {
            return new TransactionResponse(false, source.getBalance(), newSourceBalance, "Solde insuffisant (limite de découvert dépassée)");
        }

        if (applied) {
            source.setBalance(newSourceBalance);
            dest.setBalance(dest.getBalance().add(amount));
        }

        Transaction t = new Transaction();
        t.setAccountFrom(source);
        t.setAccountTo(dest);
        t.setAmount(amount);
        t.setComment(comment);
        t.setAuthor(author);
        t.setApplied(applied);
        t.setDate(java.time.LocalDateTime.now());

        em.persist(t);

        String successMsg = applied ? "Virement effectué" : "Virement enregistré (en attente de validation du conseiller)";

        return new TransactionResponse(true, source.getBalance(), newSourceBalance, successMsg);
    }
}