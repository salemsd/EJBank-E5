package com.ejbank.beans;

import com.ejbank.entities.Account;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@LocalBean
public class AccountBean {

    @PersistenceContext(unitName = "EJBankPU")
    private EntityManager em;

    /**
     * Récupère un compte par son ID avec chargement des relations liées (Type, Client, Conseiller).
     * @param accountId l'identifiant du compte
     * @return l'entité Account ou null
     */
    public Account getAccountById(int accountId) {
        try {
            // Justification : JOIN FETCH charge le type, le client et le conseiller rattaché
            // pour permettre le contrôle d'accès et l'affichage
            return em.createQuery(
                            "SELECT a FROM Account a " +
                                    "JOIN FETCH a.accountType " +
                                    "JOIN FETCH a.customer c " +
                                    "LEFT JOIN FETCH c.advisor " +
                                    "WHERE a.id = :id", Account.class)
                    .setParameter("id", accountId)
                    .getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }
}