package com.ejbank.beans;

import com.ejbank.entities.Account;
import com.ejbank.entities.Advisor;
import com.ejbank.entities.Customer;
import com.ejbank.entities.User;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Stateless
@LocalBean
public class UserBean {

    @PersistenceContext(unitName = "EJBankPU")
    private EntityManager em;

    public User getUserById(int id) {
        return em.find(User.class, id);
    }

    /**
     * Récupère les entités Account.
     * Utilise JOIN FETCH pour charger le AccountType en une seule requête 
     * et éviter le LazyInitializationException dans la vue.
     */
    public List<Account> getAccountsByUserId(int userId) {
        return em.createQuery("SELECT a FROM Account a JOIN FETCH a.accountType WHERE a.customer.id = :userId", Account.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    /**
     * Récupère tous les comptes des clients rattachés à un conseiller.
     * * @param advisorId ID du conseiller
     * @return liste des comptes rattachés
     */
    public List<Account> getAttachedAccountsByAdvisorId(int advisorId) {
        return em.createQuery(
                        "SELECT a FROM Account a " +
                                "JOIN FETCH a.accountType " +
                                "JOIN FETCH a.customer c " +
                                "WHERE c.advisor.id = :advisorId", Account.class)
                .setParameter("advisorId", advisorId)
                .getResultList();
    }

    public interface AccountQueryStrategy {
        List<Account> getVisibleAccounts(EntityManager em, int userId);
    }

    public class CustomerAccountStrategy implements AccountQueryStrategy {
        @Override
        public List<Account> getVisibleAccounts(EntityManager em, int userId) {
            return em.createQuery("SELECT a FROM Account a JOIN FETCH a.accountType JOIN FETCH a.customer WHERE a.customer.id = :id", Account.class)
                    .setParameter("id", userId)
                    .getResultList();
        }
    }

    public class AdvisorAccountStrategy implements AccountQueryStrategy {
        @Override
        public List<Account> getVisibleAccounts(EntityManager em, int userId) {
            return em.createQuery("SELECT a FROM Account a JOIN FETCH a.accountType JOIN FETCH a.customer c WHERE c.advisor.id = :id", Account.class)
                    .setParameter("id", userId)
                    .getResultList();
        }
    }

    private Map<Class<? extends User>, AccountQueryStrategy> strategies = Map.of(
            Customer.class, new CustomerAccountStrategy(),
            Advisor.class, new AdvisorAccountStrategy()
    );

    /**
     * Récupère tous les comptes visibles pour un utilisateur donné.
     * Si l'utilisateur est un client, retourne ses propres comptes.
     * Si c'est un conseiller, retourne les comptes de tous ses clients rattachés.
     *
     * @param userId l'ID de l'utilisateur
     * @return la liste des comptes accessibles
     */
    public List<Account> getAllVisibleAccounts(int userId) {
        User user = em.find(User.class, userId);
        if (user == null) return Collections.emptyList();

        AccountQueryStrategy strategy = strategies.get(user.getClass());
        return strategy != null ? strategy.getVisibleAccounts(em, userId) : Collections.emptyList();
    }

}