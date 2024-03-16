package io.Adrestus.Backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Blocks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BlockModel {

    @Id
    private String blockhash;

    @Column(name = "height", nullable = false)
    private int height;

    @Column(name = "timestamp", nullable = false)
    private Timestamp timestamp;

    @OneToMany(mappedBy = "blockModel")
    @ToString.Exclude
    @JsonIgnore
    private List<TransactionModel> transactions;


    public String getBlockhash() {
        return blockhash;
    }

    public void setBlockhash(String blockhash) {
        this.blockhash = blockhash;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public List<TransactionModel> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionModel> transactions) {
        this.transactions = transactions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockModel that = (BlockModel) o;
        return height == that.height && Objects.equals(blockhash, that.blockhash) && Objects.equals(timestamp, that.timestamp) && Objects.equals(transactions, that.transactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockhash, height, timestamp, transactions);
    }


    @Override
    public String toString() {
        return "BlockModel{" +
                "blockhash='" + blockhash + '\'' +
                ", height=" + height +
                ", timestamp=" + timestamp +
                '}';
    }
}
