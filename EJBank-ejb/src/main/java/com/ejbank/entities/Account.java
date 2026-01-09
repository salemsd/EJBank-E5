package com.ejbank.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "ejbank_account")
public class Account implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "account_type_id")
    private AccountType accountType;

    @Column(name = "balance")
    private BigDecimal balance;

    // Relation pour les transactions ou il est emetteur
    @OneToMany(mappedBy = "accountFrom", fetch = FetchType.LAZY)
    private List<Transaction> transactionsFrom;

    // Relation pour les transactions ou ile est destinataire
    @OneToMany(mappedBy = "accountTo", fetch = FetchType.LAZY)
    private List<Transaction> transactionsTo;

    public Account() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public AccountType getAccountType() { return accountType; }
    public void setAccountType(AccountType accountType) { this.accountType = accountType; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public List<Transaction> getTransactionsFrom() { return transactionsFrom; }
    public void setTransactionsFrom(List<Transaction> transactionsFrom) { this.transactionsFrom = transactionsFrom; }
    public List<Transaction> getTransactionsTo() { return transactionsTo; }
    public void setTransactionsTo(List<Transaction> transactionsTo) { this.transactionsTo = transactionsTo; }
}