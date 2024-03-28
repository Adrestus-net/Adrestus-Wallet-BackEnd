package io.Adrestus.Backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "Blocks")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
//Add receipt root when find time
public class BlockModel {

    @Id
    private String blockhash;

    @Column(name = "version", nullable = false)
    private int version;

    @Column(name = "previousHash", nullable = false)
    private String previousHash;

    @Column(name = "timestamp", nullable = false)
    private String timestamp;

    @Column(name = "size", nullable = false)
    private int size;

    @Column(name = "height", nullable = false)
    private int height;

    @Column(name = "generation", nullable = false)
    private int generation;

    @Column(name = "viewID", nullable = false)
    private int viewID;

    @Column(name = "zone", nullable = false)
    private int zone;

    @Column(name = "transactionProposer", nullable = false)
    private String transactionProposer;

    @Column(name = "leaderPublicKey", nullable = false)
    private String leaderPublicKey;

    @Column(name = "merkleRoot", nullable = false)
    private String merkleRoot;

    @Column(name = "patriciaMerkleRoot", nullable = false)
    private String patriciaMerkleRoot;

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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public List<TransactionModel> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionModel> transactions) {
        this.transactions = transactions;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }

    public int getViewID() {
        return viewID;
    }

    public void setViewID(int viewID) {
        this.viewID = viewID;
    }

    public int getZone() {
        return zone;
    }

    public void setZone(int zone) {
        this.zone = zone;
    }

    public String getTransactionProposer() {
        return transactionProposer;
    }

    public void setTransactionProposer(String transactionProposer) {
        this.transactionProposer = transactionProposer;
    }

    public String getLeaderPublicKey() {
        return leaderPublicKey;
    }

    public void setLeaderPublicKey(String leaderPublicKey) {
        this.leaderPublicKey = leaderPublicKey;
    }

    public String getMerkleRoot() {
        return merkleRoot;
    }

    public void setMerkleRoot(String merkleRoot) {
        this.merkleRoot = merkleRoot;
    }

    public String getPatriciaMerkleRoot() {
        return patriciaMerkleRoot;
    }

    public void setPatriciaMerkleRoot(String patriciaMerkleRoot) {
        this.patriciaMerkleRoot = patriciaMerkleRoot;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BlockModel that = (BlockModel) o;
        return version == that.version && size == that.size && height == that.height && generation == that.generation && viewID == that.viewID && zone == that.zone && Objects.equals(blockhash, that.blockhash) && Objects.equals(previousHash, that.previousHash) && Objects.equals(timestamp, that.timestamp) && Objects.equals(transactionProposer, that.transactionProposer) && Objects.equals(leaderPublicKey, that.leaderPublicKey) && Objects.equals(merkleRoot, that.merkleRoot) && Objects.equals(patriciaMerkleRoot, that.patriciaMerkleRoot) && Objects.equals(transactions, that.transactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blockhash, version, previousHash, timestamp, size, height, generation, viewID, zone, transactionProposer, leaderPublicKey, merkleRoot, patriciaMerkleRoot, transactions);
    }

    @Override
    public String toString() {
        return "BlockModel{" +
                "blockhash='" + blockhash + '\'' +
                ", version=" + version +
                ", previousHash='" + previousHash + '\'' +
                ", timestamp=" + timestamp +
                ", size=" + size +
                ", height=" + height +
                ", generation=" + generation +
                ", viewID=" + viewID +
                ", zone=" + zone +
                ", transactionProposer='" + transactionProposer + '\'' +
                ", leaderPublicKey='" + leaderPublicKey + '\'' +
                ", merkleRoot='" + merkleRoot + '\'' +
                ", patriciaMerkleRoot='" + patriciaMerkleRoot + '\'' +
                '}';
    }
}
