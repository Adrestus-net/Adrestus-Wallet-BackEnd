package io.Adrestus.Backend.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "AccountState")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountStateModel {
    @EmbeddedId
    //@JoinColumn(name = "id",referencedColumnName = "id", updatable = false, nullable = false)
    private AccountStateObject accountStateObject;

    @Column(name = "balance", nullable = false)
    private double balance;

    @Column(name = "staked", nullable = false)
    private double staked;

    @ManyToOne(optional = false)
    @JoinColumn(name = "accountId", referencedColumnName = "accountId", insertable = false, updatable = false, nullable = false)
    private AccountModel accountModel;
//     @JoinColumn(name = "id",referencedColumnName = "id", updatable = false, nullable = false)
//     private AccountModel accountModel;
//    @OneToMany(mappedBy = "account")
//    @ToString.Exclude
//    @JsonIgnore
//    private List<TransactionModel> transactions;

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getStaked() {
        return staked;
    }

    public void setStaked(double staked) {
        this.staked = staked;
    }


    public AccountStateObject getAccountStateObject() {
        return accountStateObject;
    }

    public void setAccountStateObject(AccountStateObject accountStateObject) {
        this.accountStateObject = accountStateObject;
    }


    @Override
    public String toString() {
        return "AccountStateModel{" +
                "accountStateObject=" + accountStateObject +
                ", balance=" + balance +
                ", staked=" + staked +
                '}';
    }

}
