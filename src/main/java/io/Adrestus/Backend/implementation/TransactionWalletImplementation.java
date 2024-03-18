package io.Adrestus.Backend.implementation;

import io.Adrestus.Backend.DTO.TransactionDetailsDTO;
import io.Adrestus.Backend.MemoryBuffer.AddressMemoryInstance;
import io.Adrestus.Backend.Repository.TransactionWalletRepository;
import io.Adrestus.Backend.Service.AccountService;
import io.Adrestus.Backend.Service.AccountStateService;
import io.Adrestus.Backend.Service.TransactionService;
import io.Adrestus.Backend.Util.TransactionConverterUtil;
import io.Adrestus.Backend.payload.response.ResponseDao;
import io.Adrestus.MemoryTreePool;
import io.Adrestus.TreeFactory;
import io.Adrestus.Trie.PatriciaTreeNode;
import io.Adrestus.bloom_filter.core.BloomObject;
import io.Adrestus.config.APIConfiguration;
import io.Adrestus.core.Transaction;
import io.Adrestus.core.TransactionBlock;
import io.distributedLedger.DatabaseFactory;
import io.distributedLedger.DatabaseType;
import io.distributedLedger.IDatabase;
import io.distributedLedger.ZoneDatabaseFactory;
import io.vavr.control.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@Repository("transactionDao")
public class TransactionWalletImplementation implements TransactionWalletRepository {


    @Autowired
    private AccountStateService accountStateService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;


    @Override
    public String addTransaction(Transaction transaction) {
        AddressMemoryInstance.getInstance().getMemory().add(transaction.getFrom());
        AddressMemoryInstance.getInstance().getMemory().add(transaction.getTo());
        TransactionBlock transactionBlock = new TransactionBlock();
        transactionBlock.setHash("blockhash0");
        transactionBlock.setHeight(100);
        transactionBlock.getHeader().setTimestamp(new Timestamp(System.currentTimeMillis()).toString());
        transactionService.save(TransactionConverterUtil.convert(transaction, transactionBlock));
//        MessageListener messageListener = new MessageListener();
//        Strategy transactionStrategy = new Strategy(new TransactionStrategy(transaction, messageListener));
//        transactionStrategy.SendTransactionSync();
//        if (messageListener.getConsume_list().stream().anyMatch(val -> val.equals(APIConfiguration.MSG_FAILED)))
//            return APIConfiguration.MSG_FAILED;
        return APIConfiguration.MSG_SUCCESS;
    }

    @Override
    public ResponseDao getTransactionsByAddress(String address) {
        ArrayList<Transaction> from = new ArrayList<>();
        ArrayList<Transaction> to = new ArrayList<>();
        List<TransactionDetailsDTO> fromModel = transactionService.findTransactionsByFromAddress(address);
        List<TransactionDetailsDTO> toModel = transactionService.findTransactionsByToAddress(address);
        fromModel.stream().forEach(val -> from.add(TransactionConverterUtil.convert(val)));
        toModel.stream().forEach(val -> to.add(TransactionConverterUtil.convert(val)));
        return new ResponseDao(from, to);
    }

    @Override
    public HashMap<String, String> getTransactionsBalance(BloomObject bloomObject, String zone) {
        HashMap<String, String> map = new HashMap<>();
        if (bloomObject == null) {
            return map;
        }
        if (bloomObject.toString().isEmpty())
            return map;
        if (bloomObject.toString().equals("{}")) {
            return map;
        }
        List<String> buffer = AddressMemoryInstance.getInstance().getMemory().contains(bloomObject);
        IDatabase<String, byte[]> tree_database = new DatabaseFactory(String.class, byte[].class).getDatabase(DatabaseType.ROCKS_DB, ZoneDatabaseFactory.getPatriciaTreeZoneInstance(Integer.parseInt(zone)));
        Optional<byte[]> tree = tree_database.seekLast();
        if (tree.isEmpty())
            return map;
        MemoryTreePool memoryTreePool = (MemoryTreePool) TreeFactory.getMemoryTree(Integer.valueOf(zone));
        buffer.stream().forEach(address -> {
            Option<PatriciaTreeNode> res = memoryTreePool.getByaddress(address);
            if (res.isEmpty()) {
                map.put(address, new String("0"));
                return;
            }
            map.put(address, String.valueOf(res.get().getAmount()));
        });
        return map;
    }

    @Override
    public HashMap<String, ResponseDao> getTransactionsByBloomFilter(BloomObject bloomObject) {

        HashMap<String, ResponseDao> map = new HashMap<>();
        List<String> buffer = AddressMemoryInstance.getInstance().getMemory().contains(bloomObject);
        buffer.stream().forEach(val -> {
            map.put(val, this.getTransactionsByAddress(val));
        });
        return map;
    }

}
