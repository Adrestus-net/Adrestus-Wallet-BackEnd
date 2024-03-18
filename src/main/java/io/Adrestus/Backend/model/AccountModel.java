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

    @Column(name = "accountId")
    private Long accountId;

    @Id
    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "timestamp", nullable = false)
    private Timestamp timestamp;

    @OneToMany(mappedBy = "accountModel")
    @ToString.Exclude
    @JsonIgnore
    private List<AccountStateModel> accountStateModels;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

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
        return Objects.equals(accountId, that.accountId) && Objects.equals(address, that.address) && Objects.equals(timestamp, that.timestamp) && Objects.equals(accountStateModels, that.accountStateModels);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, address, timestamp, accountStateModels);
    }

    @Override
    public String toString() {
        return "AccountModel{" +
                "accountId=" + accountId +
                ", address='" + address + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
