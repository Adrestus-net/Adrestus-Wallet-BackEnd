package io.Adrestus.Backend.ScheduledTasks;

import io.Adrestus.Backend.Config.APIConfiguration;
import io.Adrestus.config.NetworkConfiguration;
import io.Adrestus.core.CommitteeBlock;
import io.Adrestus.core.Resourses.CachedLatestBlocks;
import io.Adrestus.network.CachedEventLoop;
import io.Adrestus.rpc.RpcAdrestusClient;
import io.distributedLedger.DatabaseFactory;
import io.distributedLedger.DatabaseInstance;
import io.distributedLedger.DatabaseType;
import io.distributedLedger.IDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SyncCommitteeBlockTask {
    private static final Logger LOG = LoggerFactory.getLogger(SyncCommitteeBlockTask.class);

    @Scheduled(fixedRate = APIConfiguration.COMMITTEE_BLOCK_RATE)
    public void syncBlock() {
        List<String> ips = CachedLatestBlocks.getInstance().getCommitteeBlock().getStructureMap().get(0).values().stream().collect(Collectors.toList());
        ArrayList<InetSocketAddress> toConnectCommittee = new ArrayList<>();
        ips.stream().forEach(ip -> {
            try {
                toConnectCommittee.add(new InetSocketAddress(InetAddress.getByName(ip), NetworkConfiguration.RPC_PORT));
            } catch (UnknownHostException e) {
                throw new RuntimeException(e);
            }
        });
        RpcAdrestusClient client1;
        try {
            client1 = new RpcAdrestusClient(new CommitteeBlock(), toConnectCommittee, CachedEventLoop.getInstance().getEventloop());
            client1.connect();
        } catch (IllegalArgumentException e) {
            LOG.info(e.toString());
            return;
        }
        List<CommitteeBlock> commitee_blocks = client1.getBlocksList(String.valueOf(CachedLatestBlocks.getInstance().getCommitteeBlock().getHeight()));

        if (client1 != null) {
            client1.close();
            client1 = null;
        }

        if (commitee_blocks.size() <= 1)
            return;


        CachedLatestBlocks.getInstance().setCommitteeBlock(commitee_blocks.get(commitee_blocks.size() - 1));

        LOG.info("Committee Block Height: " + CachedLatestBlocks.getInstance().getCommitteeBlock().getHeight());
    }
}
