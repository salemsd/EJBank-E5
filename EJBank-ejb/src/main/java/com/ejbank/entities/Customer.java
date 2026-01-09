package com.ejbank.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "ejbank_customer")
@DiscriminatorValue("customer")
@PrimaryKeyJoinColumn(name = "id")
public class Customer extends User {

    @ManyToOne
    @JoinColumn(name = "advisor_id")
    private Advisor advisor;

    @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    private List<Account> accounts;

    public Customer() {}

    public Advisor getAdvisor() { return advisor; }
    public void setAdvisor(Advisor advisor) { this.advisor = advisor; }
    public List<Account> getAccounts() { return accounts; }
    public void setAccounts(List<Account> accounts) { this.accounts = accounts; }

    @Override
    public boolean requiresValidation(BigDecimal amount) {
        return amount.compareTo(new BigDecimal(1000)) > 0;
    }
    @Override
    public boolean shouldAutoApply(BigDecimal amount) {
        return amount.compareTo(new BigDecimal(1000)) <= 0;
    }
}