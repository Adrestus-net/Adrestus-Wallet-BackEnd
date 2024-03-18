package io.Adrestus.Backend.Util;

import io.Adrestus.Backend.DTO.TransactionDetailsDTO;
import io.Adrestus.Backend.model.BlockModel;
import io.Adrestus.Backend.model.TransactionModel;
import io.Adrestus.core.RegularTransaction;
import io.Adrestus.core.Transaction;
import io.Adrestus.core.TransactionBlock;
import lombok.SneakyThrows;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class TransactionConverterUtil {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");


    public static Transaction convert(TransactionDetailsDTO transactionDetailsDTO) {
        Transaction transaction = new RegularTransaction();
        transaction.setHash(transactionDetailsDTO.getTransaction_hash());
        transaction.setFrom(transactionDetailsDTO.getFromAddress());
        transaction.setTo(transactionDetailsDTO.getToAddress());
        return transaction;
    }

    public static Transaction convert(TransactionModel transactionModel) {
        Transaction transaction = new RegularTransaction();
        transaction.setHash(transactionModel.getTransactionhash());
        transaction.setFrom(transactionModel.getFrom());
        transaction.setTo(transactionModel.getTo());
        return transaction;
    }

    @SneakyThrows
    public static TransactionModel convert(Transaction transaction, TransactionBlock transactionBlock) {
        BlockModel blockModel = new BlockModel();
        blockModel.setBlockhash(transactionBlock.getHash());
        blockModel.setHeight(transactionBlock.getHeight());
        blockModel.setTimestamp(new Timestamp(dateFormat.parse(transactionBlock.getHeader().getTimestamp()).getTime()));
        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setFrom(transaction.getFrom());
        transactionModel.setTo(transaction.getTo());
        transactionModel.setTransactionhash(transaction.getHash());
        transactionModel.setBlockModel(blockModel);
        return transactionModel;
    }

}
