package io.Adrestus.Backend.DTO;

import io.Adrestus.core.StatusType;
import io.Adrestus.core.TransactionType;

public interface LimitTransactionsDetailsDTO {
    String getTransaction_hash();

    TransactionType getTransactionType();

    StatusType getStatusType();

    String getCreationDate();

    int getZoneFrom();

    int getZoneTo();

    String getFromAddress();

    String getToAddress();

    double getAmount();

    double getAmountWithTransactionFee();

    int getBlockHeight();

}
