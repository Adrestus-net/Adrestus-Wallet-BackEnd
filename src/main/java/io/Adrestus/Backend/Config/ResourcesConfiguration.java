package io.Adrestus.Backend.Config;

import com.google.common.reflect.TypeToken;
import io.Adrestus.Backend.MemoryBuffer.AddressMemoryInstance;
import io.Adrestus.core.Transaction;
import io.distributedLedger.DatabaseFactory;
import io.distributedLedger.DatabaseType;
import io.distributedLedger.IDatabase;
import io.distributedLedger.LevelDBTransactionWrapper;
import lombok.SneakyThrows;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ResourcesConfiguration implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    private final IDatabase<String, LevelDBTransactionWrapper<Transaction>> transaction_database;

    public ResourcesConfiguration() {
        transaction_database = new DatabaseFactory(String.class, Transaction.class, new TypeToken<LevelDBTransactionWrapper<Transaction>>() {
        }.getType()).getDatabase(DatabaseType.LEVEL_DB);
    }

    @SneakyThrows
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        AddressMemoryInstance.getInstance().setMemory(transaction_database.retrieveAllKeys());
    }
}
