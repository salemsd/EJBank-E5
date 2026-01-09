package com.ejbank.entities;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "ejbank_advisor")
@DiscriminatorValue("advisor")
@PrimaryKeyJoinColumn(name = "id")
public class Advisor extends User {

    @OneToMany(mappedBy = "advisor", fetch = FetchType.LAZY)
    private List<Customer> customers;

    public Advisor() {}

    public List<Customer> getCustomers() { return customers; }
    public void setCustomers(List<Customer> customers) { this.customers = customers; }

    @Override
    public boolean requiresValidation(BigDecimal amount) {
        return false;
    }
    @Override
    public boolean shouldAutoApply(BigDecimal amount) {
        return true;
    }

}