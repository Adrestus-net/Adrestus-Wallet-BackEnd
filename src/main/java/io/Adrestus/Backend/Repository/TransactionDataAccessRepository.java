package io.Adrestus.Backend.Repository;

import com.google.common.reflect.TypeToken;
import io.Adrestus.Backend.MemoryBuffer.AddressMemoryInstance;
import io.Adrestus.Backend.payload.response.ResponseDao;
import io.Adrestus.MemoryTreePool;
import io.Adrestus.Trie.PatriciaTreeNode;
import io.Adrestus.bloom_filter.core.BloomObject;
import io.Adrestus.config.APIConfiguration;
import io.Adrestus.core.Transaction;
import io.Adrestus.mapper.MemoryTreePoolSerializer;
import io.Adrestus.util.SerializationUtil;
import io.distributedLedger.*;
import io.vavr.control.Option;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@Repository("transactionDao")
public class TransactionDataAccessRepository implements KVRepository {

    private static List<Transaction> memorydb = new ArrayList<>();
    private IDatabase<String, LevelDBTransactionWrapper<Transaction>> database;
    private final SerializationUtil patricia_tree_wrapper;

    public TransactionDataAccessRepository() {
        Type fluentType = new TypeToken<MemoryTreePool>() {
        }.getType();
        List<SerializationUtil.Mapping> list = new ArrayList<>();
        list.add(new SerializationUtil.Mapping(MemoryTreePool.class, ctx -> new MemoryTreePoolSerializer()));
        List<SerializationUtil.Mapping> list2 = new ArrayList<>();
        this.patricia_tree_wrapper = new SerializationUtil<>(fluentType, list);
        this.database = new DatabaseFactory(String.class, Transaction.class, new TypeToken<LevelDBTransactionWrapper<Transaction>>() {
        }.getType()).getDatabase(DatabaseType.LEVEL_DB);
    }

    @Override
    public String addTransaction(Transaction transaction) {
        AddressMemoryInstance.getInstance().getMemory().add(transaction.getFrom());
        AddressMemoryInstance.getInstance().getMemory().add(transaction.getTo());
        database.save(transaction.getFrom(), transaction);
        database.save(transaction.getTo(), transaction);
//        MessageListener messageListener = new MessageListener();
//        Strategy transactionStrategy = new Strategy(new TransactionStrategy(transaction, messageListener));
//        transactionStrategy.SendTransactionSync();
//        if (messageListener.getConsume_list().stream().anyMatch(val -> val.equals(APIConfiguration.MSG_FAILED)))
//            return APIConfiguration.MSG_FAILED;
        return APIConfiguration.MSG_SUCCESS;
    }

    @Override
    public int updateTransactionByAddress(String from, Transaction transaction) {
        database.save(transaction.getFrom(), transaction);
        database.save(transaction.getTo(), transaction);
        return 1;
    }

    @Override
    public ResponseDao getTransactionsByAddress(String address) {
        Optional<LevelDBTransactionWrapper<Transaction>> wrapper = database.findByKey(address);
        if (wrapper.isPresent()) {
            ArrayList<Transaction> from = new ArrayList<>();
            ArrayList<Transaction> to = new ArrayList<>();
            wrapper.get().getFrom().stream().forEach(val -> from.add(val));
            wrapper.get().getTo().stream().forEach(val -> to.add(val));
            return new ResponseDao(from, to);
        }
        return null;
    }

    @Override
    public HashMap<String, String> getTransactionsBalance(BloomObject bloomObject,String zone) {
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
        MemoryTreePool memoryTreePool = (MemoryTreePool) patricia_tree_wrapper.decode(tree.get());
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

    @Override
    public int deleteALL() {
        database.erase_db();
        return 0;
    }
}
