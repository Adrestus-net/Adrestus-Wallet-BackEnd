package io.Adrestus.Backend.Util;

import io.Adrestus.Backend.DTO.TransactionDetailsDTO;
import io.Adrestus.Backend.model.BlockModel;
import io.Adrestus.Backend.model.TransactionModel;
import io.Adrestus.core.RegularTransaction;
import io.Adrestus.core.Transaction;
import lombok.SneakyThrows;
import org.apache.commons.codec.binary.Hex;

import java.math.BigInteger;

public class TransactionConverterUtil {

    public static Transaction convert(TransactionDetailsDTO transactionDetailsDTO) {
        Transaction transaction = new RegularTransaction();
        transaction.setHash(transactionDetailsDTO.getTransaction_hash());
        transaction.setType(transactionDetailsDTO.getTransactionType());
        transaction.setStatus(transactionDetailsDTO.getStatusType());
        transaction.setZoneFrom(transactionDetailsDTO.getZoneFrom());
        transaction.setZoneTo(transactionDetailsDTO.getZoneTo());
        transaction.setTimestamp(transactionDetailsDTO.getCreationDate());
        transaction.setFrom(transactionDetailsDTO.getFromAddress());
        transaction.setTo(transactionDetailsDTO.getToAddress());
        transaction.setAmount(transactionDetailsDTO.getAmount());
        transaction.setAmountWithTransactionFee(transactionDetailsDTO.getAmountWithTransactionFee());
        transaction.setNonce(transactionDetailsDTO.getNonce());
        transaction.setXAxis(new BigInteger(transactionDetailsDTO.getXAxis()));
        transaction.setYAxis(new BigInteger(transactionDetailsDTO.getYAxis()));
        return transaction;
    }

    @SneakyThrows
    public static TransactionModel convert(Transaction transaction, BlockModel blockModel) {
        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setTransactionhash(transaction.getHash());
        transactionModel.setType(transaction.getType());
        transactionModel.setStatus(transaction.getStatus());
        transactionModel.setZoneFrom(transaction.getZoneFrom());
        transactionModel.setZoneTo(transaction.getZoneTo());
        transactionModel.setZoneFrom(transaction.getZoneFrom());
        transactionModel.setTimestamp(transaction.getTimestamp());
        transactionModel.setFrom(transaction.getFrom());
        transactionModel.setTo(transaction.getTo());
        transactionModel.setAmount(transaction.getAmount());
        transactionModel.setAmountWithTransactionFee(transaction.getAmountWithTransactionFee());
        transactionModel.setNonce(transaction.getNonce());
        transactionModel.setXAxis(transaction.getXAxis().toString());
        transactionModel.setYAxis(transaction.getYAxis().toString());
        transactionModel.setV(transaction.getSignature().getV());
        transactionModel.setR(Hex.encodeHexString(transaction.getSignature().getR()));
        transactionModel.setS(Hex.encodeHexString(transaction.getSignature().getS()));
        transactionModel.setPub(Hex.encodeHexString(transaction.getSignature().getPub()));
        transactionModel.setBlockModel(blockModel);
        return transactionModel;
    }

}
