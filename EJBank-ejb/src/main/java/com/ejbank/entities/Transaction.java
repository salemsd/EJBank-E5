package com.ejbank.entities;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "ejbank_transaction")
public class Transaction implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "account_id_from")
    private Account accountFrom;

    @ManyToOne
    @JoinColumn(name = "account_id_to")
    private Account accountTo;

    @ManyToOne
    @JoinColumn(name = "author")
    private User author;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "comment")
    private String comment;

    @Column(name = "applied")
    private boolean applied;

    @Column(name = "date")
    private LocalDateTime date;

    public Transaction() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public Account getAccountFrom() { return accountFrom; }
    public void setAccountFrom(Account accountFrom) { this.accountFrom = accountFrom; }
    public Account getAccountTo() { return accountTo; }
    public void setAccountTo(Account accountTo) { this.accountTo = accountTo; }
    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public boolean isApplied() { return applied; }
    public void setApplied(boolean applied) { this.applied = applied; }
    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }
}