package io.Adrestus.Backend.ScheduledTasks;


import com.google.common.reflect.TypeToken;
import io.Adrestus.Backend.Config.APIConfiguration;
import io.Adrestus.MemoryTreePool;
import io.Adrestus.TreeFactory;
import io.Adrestus.core.CommitteeBlock;
import io.Adrestus.core.Resourses.CachedLatestBlocks;
import io.Adrestus.core.Transaction;
import io.Adrestus.core.TransactionBlock;
import io.Adrestus.mapper.MemoryTreePoolSerializer;
import io.Adrestus.network.CachedEventLoop;
import io.Adrestus.rpc.RpcAdrestusClient;
import io.Adrestus.util.SerializationUtil;
import io.distributedLedger.*;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class SyncTransactionBlockTask {
    private static final SerializationUtil patricia_tree_wrapper;
    private static final Logger LOG = LoggerFactory.getLogger(SyncTransactionBlockTask.class);

    static {
        Type fluentType = new TypeToken<MemoryTreePool>() {
        }.getType();
        List<SerializationUtil.Mapping> list = new ArrayList<>();
        list.add(new SerializationUtil.Mapping(MemoryTreePool.class, ctx -> new MemoryTreePoolSerializer()));
        List<SerializationUtil.Mapping> list2 = new ArrayList<>();
        patricia_tree_wrapper = new SerializationUtil<>(fluentType, list);
    }

    @Scheduled(fixedRate = APIConfiguration.TRANSACTION_BLOCK_RATE)
    public void syncBlock() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(4);
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                syncBlock(0);
                latch.countDown();
            }
        });
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                syncBlock(1);
                latch.countDown();

            }
        });
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                syncBlock(2);
                latch.countDown();
            }
        });
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                syncBlock(3);
                latch.countDown();
            }
        });

        latch.await();
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(800, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
        executorService = null;
    }

    //MAKE SURE DELETEDB TEST IS RUNNING BEFORE EXECUTE THIS CODE
    @SneakyThrows
    public void syncBlock(int zone) {
        CommitteeBlock committee = (CommitteeBlock) CachedLatestBlocks.getInstance().getCommitteeBlock().clone();
        List<String> new_ips = committee.getStructureMap().get(zone).values().stream().collect(Collectors.toList());

        if (new_ips.isEmpty()) return;

        int RPCTransactionZonePort = ZoneDatabaseFactory.getDatabaseRPCPort(zone);
        int RPCPatriciaTreeZonePort = ZoneDatabaseFactory.getDatabasePatriciaRPCPort(ZoneDatabaseFactory.getPatriciaTreeZoneInstance(zone));
        ArrayList<InetSocketAddress> toConnectTransaction = new ArrayList<>();
        ArrayList<InetSocketAddress> toConnectPatricia = new ArrayList<>();

        new_ips.stream().forEach(ip -> {
            try {
                toConnectTransaction.add(new InetSocketAddress(InetAddress.getByName(ip), RPCTransactionZonePort));
                toConnectPatricia.add(new InetSocketAddress(InetAddress.getByName(ip), RPCPatriciaTreeZonePort));
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        });

        RpcAdrestusClient client = null;
        List<String> patriciaRootList = null;
        try {
            IDatabase<String, LevelDBTransactionWrapper<Transaction>> transaction_database = new DatabaseFactory(String.class, Transaction.class, new TypeToken<LevelDBTransactionWrapper<Transaction>>() {
            }.getType()).getDatabase(DatabaseType.LEVEL_DB);
            IDatabase<String, TransactionBlock> block_database = new DatabaseFactory(String.class, TransactionBlock.class).getDatabase(DatabaseType.ROCKS_DB, ZoneDatabaseFactory.getZoneInstance(zone));
            try {
                client = new RpcAdrestusClient(new TransactionBlock(), toConnectTransaction, CachedEventLoop.getInstance().getEventloop());
                client.connect();
            } catch (IllegalArgumentException e) {
                LOG.info(e.toString());
                return;
            }
            Optional<TransactionBlock> block = block_database.seekLast();
            Map<String, TransactionBlock> toSave = new HashMap<>();
            List<TransactionBlock> blocks;
            if (block.isPresent()) {
                blocks = client.getBlocksList(String.valueOf(block.get().getHeight()));
                if (!blocks.isEmpty() && blocks.size() > 1) {
                    patriciaRootList = new ArrayList<>(blocks.stream().filter(val -> val.getGeneration() > CachedLatestBlocks.getInstance().getCommitteeBlock().getGeneration()).map(TransactionBlock::getHash).collect(Collectors.toList()));
                    blocks.removeIf(x -> x.getGeneration() > CachedLatestBlocks.getInstance().getCommitteeBlock().getGeneration());
                    blocks.stream().skip(1).forEach(val -> toSave.put(String.valueOf(val.getHeight()), val));
                }

            } else {
                blocks = client.getBlocksList("");
                if (!blocks.isEmpty()) {
                    patriciaRootList = new ArrayList<>(blocks.stream().filter(val -> val.getGeneration() > CachedLatestBlocks.getInstance().getCommitteeBlock().getGeneration()).map(TransactionBlock::getHash).collect(Collectors.toList()));
                    blocks.removeIf(x -> x.getGeneration() > CachedLatestBlocks.getInstance().getCommitteeBlock().getGeneration());
                    blocks.stream().forEach(val -> toSave.put(String.valueOf(val.getHeight()), val));
                }
            }

            if (patriciaRootList == null) {
                if (client != null) {
                    client.close();
                    client = null;
                }
                return;
            }
            block_database.saveAll(toSave);

            if (!blocks.isEmpty()) {
                blocks.stream().forEach(transactionBlock -> {
                    transactionBlock.getTransactionList().stream().forEach(transaction -> {
                        transaction_database.save(transaction.getFrom(), transaction);
                        transaction_database.save(transaction.getTo(), transaction);
                    });
                });
                CachedLatestBlocks.getInstance().setTransactionBlock(blocks.get(blocks.size() - 1));
                LOG.info("Transaction Block Height: " + CachedLatestBlocks.getInstance().getTransactionBlock().getHeight());
                LOG.info("Transaction List Height: " + CachedLatestBlocks.getInstance().getTransactionBlock().getTransactionList().size());
            }

            if (client != null) {
                client.close();
                client = null;
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }


        try {
            IDatabase<String, byte[]> tree_database = new DatabaseFactory(String.class, byte[].class).getDatabase(DatabaseType.ROCKS_DB, ZoneDatabaseFactory.getPatriciaTreeZoneInstance(zone));
            client = new RpcAdrestusClient(new byte[]{}, toConnectPatricia, CachedEventLoop.getInstance().getEventloop());
            client.connect();

            Optional<byte[]> tree = tree_database.seekLast();
            List<byte[]> treeObjects;
            if (tree.isPresent()) {
                treeObjects = client.getPatriciaTreeList(((MemoryTreePool) patricia_tree_wrapper.decode(tree.get())).getHeight());
            } else {
                treeObjects = client.getPatriciaTreeList("");
            }
            Map<String, byte[]> toSave = new HashMap<>();
            if (tree.isPresent()) {
                if (!treeObjects.isEmpty() && treeObjects.size() > 1) {
                    treeObjects.stream().skip(1).forEach(val -> {
                        try {
                            toSave.put(((MemoryTreePool) patricia_tree_wrapper.decode(val)).getHeight(), val);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            } else {
                if (!treeObjects.isEmpty()) {
                    treeObjects.stream().forEach(val -> {
                        try {
                            toSave.put(((MemoryTreePool) patricia_tree_wrapper.decode(val)).getHeight(), val);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }

            if (toSave == null || patriciaRootList == null) {
                if (client != null) {
                    client.close();
                    client = null;
                }
                return;
            }

            List<String> finalPatriciaRootList = patriciaRootList;
            Map<String, byte[]> toCollect = toSave.entrySet().stream().filter(x -> !finalPatriciaRootList.contains(x.getKey())).collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
            tree_database.saveAll(toCollect);
            byte[] current_tree = toCollect.get(String.valueOf(CachedLatestBlocks.getInstance().getTransactionBlock().getHeight()));
            if (current_tree == null) {
                if (client != null) {
                    client.close();
                    client = null;
                }
                return;
            }
            TreeFactory.setMemoryTree((MemoryTreePool) patricia_tree_wrapper.decode(current_tree), zone);


            LOG.info("TreeFactory Height: " + TreeFactory.getMemoryTree(zone).getHeight());
            if (client != null) {
                client.close();
                client = null;
            }
        } catch (IllegalArgumentException e) {
        }

    }
}
