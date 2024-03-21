package io.Adrestus.Backend.model;


import io.Adrestus.core.StatusType;
import io.Adrestus.core.TransactionType;
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

    @Column(name = "transaction_type", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(name = "status", updatable = false, nullable = false)
    @Enumerated(EnumType.STRING)
    private StatusType status;

    @Column(name = "zone_from", updatable = false, nullable = false)
    private int zoneFrom;

    @Column(name = "zone_to", updatable = false, nullable = false)
    private int ZoneTo;

    @Column(name = "timestamp", updatable = false, nullable = false)
    protected String timestamp;

    @Column(name = "from", updatable = false, nullable = false)
    private String from;

    @Column(name = "to", updatable = false, nullable = false)
    private String to;

    @Column(name = "amount", updatable = false, nullable = false)
    private double amount;

    @Column(name = "amount_with_transaction_fee", updatable = false, nullable = false)
    private double AmountWithTransactionFee;

    @Column(name = "nonce", updatable = false, nullable = false)
    private int Nonce;

    @Column(name = "xaxis", updatable = false, nullable = false)
    protected String XAxis;

    @Column(name = "yaxis", updatable = false, nullable = false)
    protected String YAxis;

    @Column(name = "v", updatable = false, nullable = false)
    private byte v;

    @Column(name = "r", updatable = false, nullable = false)
    private String r;

    @Column(name = "s", updatable = false, nullable = false)
    private String s;

    @Column(name = "pub", updatable = false, nullable = false)
    private String pub;

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


    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public StatusType getStatus() {
        return status;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public int getZoneFrom() {
        return zoneFrom;
    }

    public void setZoneFrom(int zoneFrom) {
        this.zoneFrom = zoneFrom;
    }

    public int getZoneTo() {
        return ZoneTo;
    }

    public void setZoneTo(int zoneTo) {
        ZoneTo = zoneTo;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getAmountWithTransactionFee() {
        return AmountWithTransactionFee;
    }

    public void setAmountWithTransactionFee(double amountWithTransactionFee) {
        AmountWithTransactionFee = amountWithTransactionFee;
    }

    public int getNonce() {
        return Nonce;
    }

    public void setNonce(int nonce) {
        Nonce = nonce;
    }


    public byte getV() {
        return v;
    }

    public void setV(byte v) {
        this.v = v;
    }

    public String getXAxis() {
        return XAxis;
    }

    public void setXAxis(String XAxis) {
        this.XAxis = XAxis;
    }

    public String getYAxis() {
        return YAxis;
    }

    public void setYAxis(String YAxis) {
        this.YAxis = YAxis;
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r;
    }

    public String getS() {
        return s;
    }

    public void setS(String s) {
        this.s = s;
    }

    public String getPub() {
        return pub;
    }

    public void setPub(String pub) {
        this.pub = pub;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionModel that = (TransactionModel) o;
        return zoneFrom == that.zoneFrom && ZoneTo == that.ZoneTo && Double.compare(amount, that.amount) == 0 && Double.compare(AmountWithTransactionFee, that.AmountWithTransactionFee) == 0 && Nonce == that.Nonce && v == that.v && Objects.equals(transactionhash, that.transactionhash) && type == that.type && status == that.status && Objects.equals(timestamp, that.timestamp) && Objects.equals(from, that.from) && Objects.equals(to, that.to) && Objects.equals(XAxis, that.XAxis) && Objects.equals(YAxis, that.YAxis) && Objects.equals(r, that.r) && Objects.equals(s, that.s) && Objects.equals(pub, that.pub) && Objects.equals(blockModel, that.blockModel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionhash, type, status, zoneFrom, ZoneTo, timestamp, from, to, amount, AmountWithTransactionFee, Nonce, XAxis, YAxis, v, r, s, pub, blockModel);
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
