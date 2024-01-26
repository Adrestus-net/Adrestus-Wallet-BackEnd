package io.Adrestus.Backend.Controller;

import com.google.common.reflect.TypeToken;
import io.Adrestus.Backend.payload.request.BalanceRequest;
import io.Adrestus.MemoryTreePool;
import io.Adrestus.Trie.PatriciaTreeNode;
import io.Adrestus.mapper.MemoryTreePoolSerializer;
import io.Adrestus.util.SerializationUtil;
import io.distributedLedger.DatabaseFactory;
import io.distributedLedger.DatabaseType;
import io.distributedLedger.IDatabase;
import io.distributedLedger.ZoneDatabaseFactory;
import io.vavr.control.Option;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Order(Ordered.HIGHEST_PRECEDENCE)
@CrossOrigin
@RestController
public class BalanceController {
    private final SerializationUtil patricia_tree_wrapper;

    public BalanceController() {
        Type fluentType = new TypeToken<MemoryTreePool>() {
        }.getType();
        List<SerializationUtil.Mapping> list = new ArrayList<>();
        list.add(new SerializationUtil.Mapping(MemoryTreePool.class, ctx -> new MemoryTreePoolSerializer()));
        List<SerializationUtil.Mapping> list2 = new ArrayList<>();
        patricia_tree_wrapper = new SerializationUtil<>(fluentType, list);
    }

    @RequestMapping(value = "api/v1/balance", method = RequestMethod.POST)
    public @ResponseBody String createAuthenticationToken(@RequestBody BalanceRequest balanceRequest) {
        if (balanceRequest == null) {
            return "0";
        }
        if (balanceRequest.getAddress() == null)
            return "0";
        if (balanceRequest.getAddress().equals("")) {
            return "0";
        }
        IDatabase<String, byte[]> tree_database = new DatabaseFactory(String.class, byte[].class).getDatabase(DatabaseType.ROCKS_DB, ZoneDatabaseFactory.getPatriciaTreeZoneInstance(balanceRequest.getZone()));
        Optional<byte[]> tree = tree_database.seekLast();
        if (!tree.isPresent())
            return "0";
        MemoryTreePool memoryTreePool = (MemoryTreePool) patricia_tree_wrapper.decode(tree.get());
        Option<PatriciaTreeNode> res = memoryTreePool.getByaddress(balanceRequest.getAddress());
        if (res.isEmpty())
            return "0";

        return String.valueOf(res.get().getAmount());
    }
}
