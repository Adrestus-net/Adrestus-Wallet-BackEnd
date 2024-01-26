package io.Adrestus.Backend.Repository;

import com.google.common.reflect.TypeToken;
import io.Adrestus.Backend.MemoryBuffer.AddressMemoryInstance;
import io.Adrestus.Backend.payload.response.ResponseDao;
import io.Adrestus.bloom_filter.BloomFilter;
import io.Adrestus.bloom_filter.core.BloomObject;
import io.Adrestus.bloom_filter.impl.InMemoryBloomFilter;
import io.Adrestus.config.APIConfiguration;
import io.Adrestus.core.Transaction;
import io.distributedLedger.DatabaseFactory;
import io.distributedLedger.DatabaseType;
import io.distributedLedger.IDatabase;
import io.distributedLedger.LevelDBTransactionWrapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


@Repository("transactionDao")
public class TransactionDataAccessRepository implements KVRepository {

    private static List<Transaction> memorydb = new ArrayList<>();
    private IDatabase<String, LevelDBTransactionWrapper<Transaction>> database;

    public TransactionDataAccessRepository() {
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
    public HashMap<String, ResponseDao> getTransactionsByBloomFilter(BloomObject bloomObject) {
        HashMap<String, ResponseDao> map = new HashMap<>();
        BloomFilter<String> match_filter = new InMemoryBloomFilter<String>(bloomObject.getNumBitsRequired(), bloomObject.getHashFunctionNum(), bloomObject.getArray(), null);
        List<String> buffer = new ArrayList<String>(AddressMemoryInstance.getInstance().getMemory().getResources());
        for (String val : buffer) {
            if (!match_filter.contains(val)) {
                buffer.remove(val);
            }
        }
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
