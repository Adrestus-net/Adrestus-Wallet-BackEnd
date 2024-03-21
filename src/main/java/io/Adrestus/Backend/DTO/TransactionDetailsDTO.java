package io.Adrestus.Backend.DTO;

import io.Adrestus.core.StatusType;
import io.Adrestus.core.TransactionType;

public interface TransactionDetailsDTO {
    String getTransaction_hash();

    TransactionType getTransactionType();

    StatusType getStatusType();

    int getZoneFrom();

    int getZoneTo();

    String getCreationDate();

    String getFromAddress();

    String getToAddress();

    double getAmount();

    double getAmountWithTransactionFee();

    int getNonce();

    String getXAxis();

    String getYAxis();

    byte getV();

    String getR();

    String getS();

    String getPub();

    String getBlockHash();

    int getBlockHeight();


}
