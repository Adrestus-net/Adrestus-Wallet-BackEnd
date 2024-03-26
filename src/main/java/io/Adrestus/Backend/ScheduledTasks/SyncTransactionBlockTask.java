package io.Adrestus.Backend.ScheduledTasks;


import com.google.common.reflect.TypeToken;
import io.Adrestus.Backend.Config.APIConfiguration;
import io.Adrestus.Backend.Service.AccountService;
import io.Adrestus.Backend.Service.AccountStateService;
import io.Adrestus.Backend.Service.BlockService;
import io.Adrestus.Backend.Service.TransactionService;
import io.Adrestus.Backend.Util.ConverterUtil;
import io.Adrestus.Backend.model.*;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Component
public class SyncTransactionBlockTask {

    @Autowired
    @Lazy
    private AccountStateService accountStateService;

    @Autowired
    @Lazy
    private AccountService accountService;

    @Autowired
    @Lazy
    private TransactionService transactionService;

    @Autowired
    @Lazy
    private BlockService blockService;

    private static final Logger LOG = LoggerFactory.getLogger(SyncTransactionBlockTask.class);

    private static final SerializationUtil patricia_tree_wrapper;

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
        ArrayList<TransactionModel>transactionModels=new ArrayList<>();
        ArrayList<AccountModel>accountModels=new ArrayList<>();
        ArrayList<AccountStateModel>accountStateModels=new ArrayList<>();
        try {
            try {
                client = new RpcAdrestusClient(new TransactionBlock(), toConnectTransaction, CachedEventLoop.getInstance().getEventloop());
                client.connect();
            } catch (IllegalArgumentException e) {
                LOG.info(e.toString());
                return;
            }
            BlockModel block = blockService.findLatestAddedBlockByTimestamp();
            ArrayList<TransactionBlock>toSave=new ArrayList<>();
            List<TransactionBlock> blocks;
            if (block!=null) {
                blocks = client.getBlocksList(String.valueOf(block.getHeight()));
                if (!blocks.isEmpty() && blocks.size() > 1) {
                    patriciaRootList = new ArrayList<>(blocks.stream().filter(val -> val.getGeneration() > CachedLatestBlocks.getInstance().getCommitteeBlock().getGeneration()).map(TransactionBlock::getHash).collect(Collectors.toList()));
                    blocks.removeIf(x -> x.getGeneration() > CachedLatestBlocks.getInstance().getCommitteeBlock().getGeneration());
                    blocks.stream().skip(1).forEach(val -> toSave.add(val));
                }

            } else {
                blocks = client.getBlocksList("");
                if (!blocks.isEmpty()) {
                    patriciaRootList = new ArrayList<>(blocks.stream().filter(val -> val.getGeneration() > CachedLatestBlocks.getInstance().getCommitteeBlock().getGeneration()).map(TransactionBlock::getHash).collect(Collectors.toList()));
                    blocks.removeIf(x -> x.getGeneration() > CachedLatestBlocks.getInstance().getCommitteeBlock().getGeneration());
                    blocks.stream().forEach(val -> toSave.add(val));
                }
            }

            if (patriciaRootList == null) {
                if (client != null) {
                    client.close();
                    client = null;
                }
                return;
            }
           ArrayList<BlockModel>blockModels= ConverterUtil.convert(toSave);
           blockService.saveAll(blockModels);
            if (!blocks.isEmpty()) {
                blocks.stream().forEach(transactionBlock -> {
                    transactionBlock.getTransactionList().stream().forEach(transaction -> {
                        Optional<BlockModel>blockModel=blockModels.stream().filter(val->val.getBlockhash().equals(transactionBlock.getHash())).findFirst();
                        if(blockModel.isPresent()){
                            transactionModels.add(ConverterUtil.convert(transaction,blockModel.get()));
                            AccountModel accountModel1 = new AccountModel();
                            accountModel1.setTimestamp(new Timestamp(System.currentTimeMillis()));
                            accountModel1.setAddress(transaction.getFrom());
                            AccountModel accountModel2 = new AccountModel();
                            accountModel2.setTimestamp(new Timestamp(System.currentTimeMillis()));
                            accountModel2.setAddress(transaction.getTo());
                            accountModels.add(accountModel1);
                            accountModels.add(accountModel2);
                        }
                        else{
                            BlockModel blockModel1=ConverterUtil.convert(transactionBlock);
                            blockService.save(blockModel1);
                            transactionModels.add(ConverterUtil.convert(transaction,blockModel1));
                            AccountModel accountModel1 = new AccountModel();
                            accountModel1.setTimestamp(new Timestamp(System.currentTimeMillis()));
                            accountModel1.setAddress(transaction.getFrom());
                            AccountModel accountModel2 = new AccountModel();
                            accountModel2.setTimestamp(new Timestamp(System.currentTimeMillis()));
                            accountModel2.setAddress(transaction.getTo());
                            accountModels.add(accountModel1);
                            accountModels.add(accountModel2);
                        }
                    });
                });
                CachedLatestBlocks.getInstance().setTransactionBlock(blocks.get(blocks.size() - 1));
                LOG.info("Transaction Block Height: " + CachedLatestBlocks.getInstance().getTransactionBlock().getHeight());
                LOG.info("Transaction List Height: " + CachedLatestBlocks.getInstance().getTransactionBlock().getTransactionList().size());
            }
            transactionService.saveAll(transactionModels);
            accountService.saveAll(accountModels);

            if (client != null) {
                client.close();
                client = null;
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }


        try {
            client = new RpcAdrestusClient(new byte[]{}, toConnectPatricia, CachedEventLoop.getInstance().getEventloop());
            client.connect();

            List<byte[]>  treeObjects = client.getPatriciaTreeList(String.valueOf(CachedLatestBlocks.getInstance().getTransactionBlock().getHeight()));
            Map<String, byte[]> toSave = new HashMap<>();
            if (!treeObjects.isEmpty()) {
                if (treeObjects.size() > 1) {
                    treeObjects.stream().skip(1).forEach(val -> {
                        try {
                            toSave.put(((MemoryTreePool) patricia_tree_wrapper.decode(val)).getHeight(), val);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
                } else {
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
            byte[] current_tree = toCollect.get(String.valueOf(CachedLatestBlocks.getInstance().getTransactionBlock().getHeight()));
            if (current_tree == null) {
                if (client != null) {
                    client.close();
                    client = null;
                }
                return;
            }
            TreeFactory.setMemoryTree((MemoryTreePool) patricia_tree_wrapper.decode(current_tree), zone);

            for(int i=0;i<transactionModels.size();i++){
                AccountStateModel accountStateModel1 = new AccountStateModel();
                accountStateModel1.setBalance(TreeFactory.getMemoryTree(zone).getByaddress(transactionModels.get(i).getFrom()).get().getAmount());
                accountStateModel1.setStaked(50);
                accountStateModel1.setAccountStateObject(new AccountStateObject(transactionModels.get(i).getFrom(), zone));

                AccountStateModel accountStateModel2= new AccountStateModel();
                accountStateModel2.setBalance(TreeFactory.getMemoryTree(zone).getByaddress(transactionModels.get(i).getTo()).get().getAmount());
                accountStateModel2.setStaked(50);
                accountStateModel2.setAccountStateObject(new AccountStateObject(transactionModels.get(i).getTo(), zone));
                accountStateModels.add(accountStateModel2);
            }
            accountStateService.saveAll(accountStateModels);
            LOG.info("TreeFactory Height: " + TreeFactory.getMemoryTree(zone).getHeight());
            if (client != null) {
                client.close();
                client = null;
            }
        } catch (IllegalArgumentException e) {
        }

    }
}
