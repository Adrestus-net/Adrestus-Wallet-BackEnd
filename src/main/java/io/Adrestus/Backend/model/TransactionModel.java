package io.Adrestus.Backend.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "Transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionModel {
    @Id
    private String transactionhash;

    @Column(name = "from", nullable = false)
    private String from;

    @Column(name = "to", nullable = false)
    private String to;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "block_hash")
    private BlockModel blockModel;


    public String getTransactionhash() {
        return transactionhash;
    }

    public void setTransactionhash(String transactionhash) {
        this.transactionhash = transactionhash;
    }

    public BlockModel getBlockModel() {
        return blockModel;
    }

    public void setBlockModel(BlockModel blockModel) {
        this.blockModel = blockModel;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionModel that = (TransactionModel) o;
        return Objects.equals(transactionhash, that.transactionhash) && Objects.equals(from, that.from) && Objects.equals(to, that.to) && Objects.equals(blockModel, that.blockModel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionhash, from, to, blockModel);
    }

    @Override
    public String toString() {
        return "TransactionModel{" +
                "transactionhash='" + transactionhash + '\'' +
                ", blockModel=" + blockModel +
                ", to='" + to + '\'' +
                '}';
    }
}
