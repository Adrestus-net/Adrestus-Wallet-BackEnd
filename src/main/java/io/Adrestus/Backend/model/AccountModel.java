package io.Adrestus.Backend.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountModel {
    @Id
    private String address;

    @Column(name = "balance", nullable = false)
    private double balance;

    @Column(name = "staked", nullable = false)
    private double staked;

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


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountModel that = (AccountModel) o;
        return Double.compare(balance, that.balance) == 0 && Double.compare(staked, that.staked) == 0 && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, balance, staked);
    }

    @Override
    public String toString() {
        return "AccountModel{" +
                "Address='" + address + '\'' +
                ", balance=" + balance +
                ", staked=" + staked +
                '}';
    }
}
