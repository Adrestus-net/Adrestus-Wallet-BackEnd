package io.Adrestus.Backend.DTO;

public interface LimitBlockDetailsDTO {
    int getZone();

    String getHash();

    int getHeight();

    String getTimestamp();

    String getMiner();

    String getMerkleRoot();

    int getTransactions();

}
