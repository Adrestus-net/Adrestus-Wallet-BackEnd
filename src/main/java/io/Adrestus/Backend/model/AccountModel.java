package io.Adrestus.Backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountModel implements Serializable {

//    @Column(name = "accountId",updatable = false,nullable = false)
//    private Long accountId;

    @Id
    @Column(name = "address", updatable = false, nullable = false)
    private String address;

    @Column(name = "timestamp", updatable = false, nullable = false)
    private Timestamp timestamp;


    public AccountModel(String address, Timestamp timestamp) {
        this.address = address;
        this.timestamp = timestamp;
    }

    @OneToMany(mappedBy = "accountModel")
    @ToString.Exclude
    @JsonIgnore
    private List<AccountStateModel> accountStateModels;


    public List<AccountStateModel> getAccountStateModels() {
        return accountStateModels;
    }

    public void setAccountStateModels(List<AccountStateModel> accountStateModels) {
        this.accountStateModels = accountStateModels;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountModel that = (AccountModel) o;
        return Objects.equals(address, that.address) && Objects.equals(timestamp, that.timestamp) && Objects.equals(accountStateModels, that.accountStateModels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(address, timestamp, accountStateModels);
    }

    @Override
    public String toString() {
        return "AccountModel{" +
                "address='" + address + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
