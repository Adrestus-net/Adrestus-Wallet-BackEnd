package io.Adrestus.Backend.Config;


import com.google.common.reflect.TypeToken;
import io.Adrestus.Backend.Service.AccountService;
import io.Adrestus.Backend.Service.AccountStateService;
import io.Adrestus.Backend.model.AccountModel;
import io.Adrestus.Backend.model.AccountStateModel;
import io.Adrestus.Backend.model.AccountStateObject;
import io.Adrestus.MemoryTreePool;
import io.Adrestus.TreeFactory;
import io.Adrestus.Trie.PatriciaTreeNode;
import io.Adrestus.config.AdrestusConfiguration;
import io.Adrestus.config.KademliaConfiguration;
import io.Adrestus.core.CommitteeBlock;
import io.Adrestus.core.Resourses.CachedLatestBlocks;
import io.Adrestus.core.TransactionBlock;
import io.Adrestus.crypto.HashUtil;
import io.Adrestus.crypto.SecurityAuditProofs;
import io.Adrestus.crypto.WalletAddress;
import io.Adrestus.crypto.bls.model.BLSPrivateKey;
import io.Adrestus.crypto.bls.model.BLSPublicKey;
import io.Adrestus.crypto.elliptic.ECDSASign;
import io.Adrestus.crypto.elliptic.ECDSASignatureData;
import io.Adrestus.crypto.elliptic.ECKeyPair;
import io.Adrestus.crypto.elliptic.Keys;
import io.Adrestus.crypto.elliptic.mapper.StakingData;
import io.Adrestus.crypto.mnemonic.Mnemonic;
import io.Adrestus.crypto.mnemonic.Security;
import io.Adrestus.crypto.mnemonic.WordList;
import io.Adrestus.crypto.vdf.engine.VdfEngine;
import io.Adrestus.mapper.MemoryTreePoolSerializer;
import io.Adrestus.network.CachedEventLoop;
import io.Adrestus.p2p.kademlia.common.NettyConnectionInfo;
import io.Adrestus.p2p.kademlia.node.KeyHashGenerator;
import io.Adrestus.p2p.kademlia.repository.KademliaData;
import io.Adrestus.util.GetTime;
import io.Adrestus.util.SerializationUtil;
import io.distributedLedger.*;
import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableJpaRepositories("io.Adrestus.Backend.Repository")
public class ConsensusConfiguration implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {
    private static BLSPrivateKey sk1;
    private static BLSPublicKey vk1;

    private static BLSPrivateKey sk2;
    private static BLSPublicKey vk2;

    private static BLSPrivateKey sk3;
    private static BLSPublicKey vk3;

    private static BLSPrivateKey sk4;
    private static BLSPublicKey vk4;

    private static BLSPrivateKey sk5;
    private static BLSPublicKey vk5;

    private static BLSPrivateKey sk6;
    private static BLSPublicKey vk6;

    private static ECKeyPair ecKeyPair1, ecKeyPair2, ecKeyPair3, ecKeyPair4, ecKeyPair5, ecKeyPair6, ecKeyPair7, ecKeyPair8, ecKeyPair9, ecKeyPair10, ecKeyPair11, ecKeyPair12;
    private static String address1, address2, address3, address4, address5, address6, address7, address8, address9, address10, address11, address12;
    private static ECDSASign ecdsaSign = new ECDSASign();
    private static VdfEngine vdf;
    private static KademliaData kad1, kad2, kad3, kad4, kad5, kad6;
    private static KeyHashGenerator<BigInteger, String> keyHashGenerator;
    private static char[] passphrase;
    private static byte[] key1, key2, key3, key4, key5, key6, key7, key8, key9, key10, key11, key12;

    @Autowired
    @Lazy
    private AccountStateService accountStateService;

    @Autowired
    @Lazy
    private AccountService accountService;

    public ConsensusConfiguration() {
    }

    @Bean
    public void Init() throws Exception {
        Type fluentType = new TypeToken<MemoryTreePool>() {
        }.getType();
        List<SerializationUtil.Mapping> list2 = new ArrayList<>();
        list2.add(new SerializationUtil.Mapping(MemoryTreePool.class, ctx -> new MemoryTreePoolSerializer()));
        SerializationUtil patricia_tree_wrapper = new SerializationUtil<>(fluentType, list2);


        IDatabase<String, CommitteeBlock> committee_database = new DatabaseFactory(String.class, CommitteeBlock.class).getDatabase(DatabaseType.ROCKS_DB, DatabaseInstance.COMMITTEE_BLOCK);
        int version = 0x00;
        sk1 = new BLSPrivateKey(1);
        vk1 = new BLSPublicKey(sk1);

        sk2 = new BLSPrivateKey(2);
        vk2 = new BLSPublicKey(sk2);

        sk3 = new BLSPrivateKey(3);
        vk3 = new BLSPublicKey(sk3);

        sk4 = new BLSPrivateKey(4);
        vk4 = new BLSPublicKey(sk4);

        sk5 = new BLSPrivateKey(5);
        vk5 = new BLSPublicKey(sk5);

        sk6 = new BLSPrivateKey(6);
        vk6 = new BLSPublicKey(sk6);


        char[] mnemonic1 = "sample sail jungle learn general promote task puppy own conduct green affair ".toCharArray();
        char[] mnemonic2 = "photo monitor cushion indicate civil witness orchard estate online favorite sustain extend".toCharArray();
        char[] mnemonic3 = "initial car bulb nature animal honey learn awful grit arrow phrase entire ".toCharArray();
        char[] mnemonic4 = "enrich pulse twin version inject horror village aunt brief magnet blush else ".toCharArray();
        char[] mnemonic5 = "struggle travel ketchup tomato satoshi caught fog process grace pupil item ahead ".toCharArray();
        char[] mnemonic6 = "abstract raise duty scare year add fluid danger include smart senior ensure".toCharArray();
        char[] mnemonic7 = "fluid abstract raise duty scare year add danger include smart senior ensure".toCharArray();
        char[] mnemonic8 = "danger fluid abstract raise duty scare year add include smart senior ensure".toCharArray();
        char[] mnemonic9 = "abstract fluid danger raise duty scare year add include smart senior ensure".toCharArray();
        char[] mnemonic10 = "raise fluid abstract danger duty scare year add include smart senior ensure".toCharArray();
        char[] mnemonic11 = "duty fluid abstract raise danger scare year add include smart senior ensure".toCharArray();
        char[] mnemonic12 = "scare fluid abstract raise duty danger year add include smart senior ensure".toCharArray();
        passphrase = "p4ssphr4se".toCharArray();

        Mnemonic mnem = new Mnemonic(Security.NORMAL, WordList.ENGLISH);
        key1 = mnem.createSeed(mnemonic1, passphrase);
        key2 = mnem.createSeed(mnemonic2, passphrase);
        key3 = mnem.createSeed(mnemonic3, passphrase);
        key4 = mnem.createSeed(mnemonic4, passphrase);
        key5 = mnem.createSeed(mnemonic5, passphrase);
        key6 = mnem.createSeed(mnemonic6, passphrase);
        key7 = mnem.createSeed(mnemonic7, passphrase);
        key8 = mnem.createSeed(mnemonic8, passphrase);
        key9 = mnem.createSeed(mnemonic9, passphrase);
        key10 = mnem.createSeed(mnemonic10, passphrase);
        key11 = mnem.createSeed(mnemonic11, passphrase);
        key12 = mnem.createSeed(mnemonic12, passphrase);

        SecureRandom random = SecureRandom.getInstance(AdrestusConfiguration.ALGORITHM, AdrestusConfiguration.PROVIDER);
        SecureRandom random2 = SecureRandom.getInstance(AdrestusConfiguration.ALGORITHM, AdrestusConfiguration.PROVIDER);
        SecureRandom random3 = SecureRandom.getInstance(AdrestusConfiguration.ALGORITHM, AdrestusConfiguration.PROVIDER);
        random.setSeed(key1);
        ecKeyPair1 = Keys.createEcKeyPair(random);
        random.setSeed(key2);
        ecKeyPair2 = Keys.createEcKeyPair(random);
        random.setSeed(key3);
        ecKeyPair3 = Keys.createEcKeyPair(random);
        random.setSeed(key4);
        ecKeyPair4 = Keys.createEcKeyPair(random);
        random.setSeed(key5);
        ecKeyPair5 = Keys.createEcKeyPair(random);
        random.setSeed(key6);
        ecKeyPair6 = Keys.createEcKeyPair(random);
        random2.setSeed(key7);
        ecKeyPair7 = Keys.createEcKeyPair(random2);
        random3.setSeed(key8);
        ecKeyPair8 = Keys.createEcKeyPair(random3);
        random = SecureRandom.getInstance(AdrestusConfiguration.ALGORITHM, AdrestusConfiguration.PROVIDER);
        random.setSeed(key9);
        ecKeyPair9 = Keys.createEcKeyPair(random);
        random = SecureRandom.getInstance(AdrestusConfiguration.ALGORITHM, AdrestusConfiguration.PROVIDER);
        random.setSeed(key10);
        ecKeyPair10 = Keys.createEcKeyPair(random);
        random = SecureRandom.getInstance(AdrestusConfiguration.ALGORITHM, AdrestusConfiguration.PROVIDER);
        random.setSeed(key11);
        ecKeyPair11 = Keys.createEcKeyPair(random);
        random = SecureRandom.getInstance(AdrestusConfiguration.ALGORITHM, AdrestusConfiguration.PROVIDER);
        random.setSeed(key12);
        ecKeyPair12 = Keys.createEcKeyPair(random);



        List<AccountModel>accountModels=new ArrayList<>();
        List<AccountStateModel>accountStateModels=new ArrayList<>();
        address1 = WalletAddress.generate_address((byte) version, ecKeyPair1.getPublicKey());
        address2 = WalletAddress.generate_address((byte) version, ecKeyPair2.getPublicKey());
        address3 = WalletAddress.generate_address((byte) version, ecKeyPair3.getPublicKey());
        address4 = WalletAddress.generate_address((byte) version, ecKeyPair4.getPublicKey());
        address5 = WalletAddress.generate_address((byte) version, ecKeyPair5.getPublicKey());
        address6 = WalletAddress.generate_address((byte) version, ecKeyPair6.getPublicKey());
        address7 = WalletAddress.generate_address((byte) version, ecKeyPair7.getPublicKey());
        address8 = WalletAddress.generate_address((byte) version, ecKeyPair8.getPublicKey());
        address9 = WalletAddress.generate_address((byte) version, ecKeyPair9.getPublicKey());
        address10 = WalletAddress.generate_address((byte) version, ecKeyPair10.getPublicKey());
        address11 = WalletAddress.generate_address((byte) version, ecKeyPair11.getPublicKey());
        address12 = WalletAddress.generate_address((byte) version, ecKeyPair12.getPublicKey());

        accountModels.add(new AccountModel(address1,new Timestamp(System.currentTimeMillis())));
        accountModels.add(new AccountModel(address2,new Timestamp(System.currentTimeMillis())));
        accountModels.add(new AccountModel(address3,new Timestamp(System.currentTimeMillis())));
        accountModels.add(new AccountModel(address4,new Timestamp(System.currentTimeMillis())));
        accountModels.add(new AccountModel(address5,new Timestamp(System.currentTimeMillis())));
        accountModels.add(new AccountModel(address6,new Timestamp(System.currentTimeMillis())));
        accountModels.add(new AccountModel(address7,new Timestamp(System.currentTimeMillis())));
        accountModels.add(new AccountModel(address8,new Timestamp(System.currentTimeMillis())));
        accountModels.add(new AccountModel(address9,new Timestamp(System.currentTimeMillis())));
        accountModels.add(new AccountModel(address10,new Timestamp(System.currentTimeMillis())));
        accountModels.add(new AccountModel(address11,new Timestamp(System.currentTimeMillis())));
        accountModels.add(new AccountModel(address12,new Timestamp(System.currentTimeMillis())));
        accountModels.add(new AccountModel("ADR-GBIV-HG2J-27P5-BNVN-MLN6-DL5V-M3YZ-PKEJ-CFFG-FK4L",new Timestamp(System.currentTimeMillis())));
        accountModels.add(new AccountModel("ADR-GBZX-XXCW-LWJC-J7RZ-Q6BJ-RFBA-J5WU-NBAG-4RL7-7G6Z",new Timestamp(System.currentTimeMillis())));
        accountModels.add(new AccountModel("ADR-GD3G-DK4I-DKM2-IQSB-KBWL-HWRV-BBQA-MUAS-MGXA-5QPP",new Timestamp(System.currentTimeMillis())));
        accountModels.add(new AccountModel("ADR-GC2I-WBAW-IKJE-BWFC-ML6T-BNOC-7XOU-IQ74-BJ5L-WP7G",new Timestamp(System.currentTimeMillis())));



        ECDSASignatureData signatureData1 = ecdsaSign.secp256SignMessage(HashUtil.sha256(StringUtils.getBytesUtf8(address1)), ecKeyPair1);
        ECDSASignatureData signatureData2 = ecdsaSign.secp256SignMessage(HashUtil.sha256(StringUtils.getBytesUtf8(address2)), ecKeyPair2);
        ECDSASignatureData signatureData3 = ecdsaSign.secp256SignMessage(HashUtil.sha256(StringUtils.getBytesUtf8(address3)), ecKeyPair3);
        ECDSASignatureData signatureData4 = ecdsaSign.secp256SignMessage(HashUtil.sha256(StringUtils.getBytesUtf8(address4)), ecKeyPair4);
        ECDSASignatureData signatureData5 = ecdsaSign.secp256SignMessage(HashUtil.sha256(StringUtils.getBytesUtf8(address5)), ecKeyPair5);
        ECDSASignatureData signatureData6 = ecdsaSign.secp256SignMessage(HashUtil.sha256(StringUtils.getBytesUtf8(address6)), ecKeyPair6);
        ECDSASignatureData signatureData7 = ecdsaSign.secp256SignMessage(HashUtil.sha256(StringUtils.getBytesUtf8(address7)), ecKeyPair7);

        TreeFactory.getMemoryTree(0).store(address1, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(0).store(address2, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(0).store(address3, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(0).store(address4, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(0).store(address5, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(0).store(address6, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(0).store(address7, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(0).store(address8, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(0).store(address9, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(0).store(address10, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(0).store(address11, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(0).store(address12, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(0).store("ADR-GBIV-HG2J-27P5-BNVN-MLN6-DL5V-M3YZ-PKEJ-CFFG-FK4L", new PatriciaTreeNode(1000, 0));
        TreeFactory.getMemoryTree(0).store("ADR-GBZX-XXCW-LWJC-J7RZ-Q6BJ-RFBA-J5WU-NBAG-4RL7-7G6Z", new PatriciaTreeNode(1000, 0));
        TreeFactory.getMemoryTree(0).store("ADR-GD3G-DK4I-DKM2-IQSB-KBWL-HWRV-BBQA-MUAS-MGXA-5QPP", new PatriciaTreeNode(1000, 0));
        TreeFactory.getMemoryTree(0).store("ADR-GC2I-WBAW-IKJE-BWFC-ML6T-BNOC-7XOU-IQ74-BJ5L-WP7G", new PatriciaTreeNode(1000, 0));

        accountStateModels.add(new AccountStateModel(new AccountStateObject(address1,0),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address2,0),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address3,0),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address4,0),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address5,0),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address6,0),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address7,0),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address8,0),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address9,0),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address10,0),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address11,0),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address12,0),3000,3000));

        accountStateModels.add(new AccountStateModel(new AccountStateObject("ADR-GBIV-HG2J-27P5-BNVN-MLN6-DL5V-M3YZ-PKEJ-CFFG-FK4L",0),1000,1000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject("ADR-GBZX-XXCW-LWJC-J7RZ-Q6BJ-RFBA-J5WU-NBAG-4RL7-7G6Z",0),1000,1000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject("ADR-GD3G-DK4I-DKM2-IQSB-KBWL-HWRV-BBQA-MUAS-MGXA-5QPP",0),1000,1000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject("ADR-GC2I-WBAW-IKJE-BWFC-ML6T-BNOC-7XOU-IQ74-BJ5L-WP7G",0),1000,1000));

        TreeFactory.getMemoryTree(1).store(address1, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(1).store(address2, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(1).store(address3, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(1).store(address4, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(1).store(address5, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(1).store(address6, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(1).store(address7, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(1).store(address8, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(1).store(address9, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(1).store(address10, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(1).store(address11, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(1).store(address12, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(1).store("ADR-GBIV-HG2J-27P5-BNVN-MLN6-DL5V-M3YZ-PKEJ-CFFG-FK4L", new PatriciaTreeNode(1000, 0));
        TreeFactory.getMemoryTree(1).store("ADR-GBZX-XXCW-LWJC-J7RZ-Q6BJ-RFBA-J5WU-NBAG-4RL7-7G6Z", new PatriciaTreeNode(2000, 0));
        TreeFactory.getMemoryTree(1).store("ADR-GD3G-DK4I-DKM2-IQSB-KBWL-HWRV-BBQA-MUAS-MGXA-5QPP", new PatriciaTreeNode(2000, 0));
        TreeFactory.getMemoryTree(1).store("ADR-GC2I-WBAW-IKJE-BWFC-ML6T-BNOC-7XOU-IQ74-BJ5L-WP7G", new PatriciaTreeNode(2000, 0));

        accountStateModels.add(new AccountStateModel(new AccountStateObject(address1,1),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address2,1),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address3,1),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address4,1),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address5,1),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address6,1),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address7,1),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address8,1),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address9,1),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address10,1),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address11,1),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address12,1),3000,3000));

        accountStateModels.add(new AccountStateModel(new AccountStateObject("ADR-GBIV-HG2J-27P5-BNVN-MLN6-DL5V-M3YZ-PKEJ-CFFG-FK4L",1),1000,1000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject("ADR-GBZX-XXCW-LWJC-J7RZ-Q6BJ-RFBA-J5WU-NBAG-4RL7-7G6Z",1),2000,2000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject("ADR-GD3G-DK4I-DKM2-IQSB-KBWL-HWRV-BBQA-MUAS-MGXA-5QPP",1),2000,2000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject("ADR-GC2I-WBAW-IKJE-BWFC-ML6T-BNOC-7XOU-IQ74-BJ5L-WP7G",1),2000,2000));


        TreeFactory.getMemoryTree(2).store(address1, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(2).store(address2, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(2).store(address3, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(2).store(address4, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(2).store(address5, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(2).store(address6, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(2).store(address7, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(2).store(address8, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(2).store(address9, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(2).store(address10, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(2).store(address11, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(2).store(address12, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(2).store("ADR-GBIV-HG2J-27P5-BNVN-MLN6-DL5V-M3YZ-PKEJ-CFFG-FK4L", new PatriciaTreeNode(1000, 0));
        TreeFactory.getMemoryTree(2).store("ADR-GBZX-XXCW-LWJC-J7RZ-Q6BJ-RFBA-J5WU-NBAG-4RL7-7G6Z", new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(2).store("ADR-GD3G-DK4I-DKM2-IQSB-KBWL-HWRV-BBQA-MUAS-MGXA-5QPP", new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(2).store("ADR-GC2I-WBAW-IKJE-BWFC-ML6T-BNOC-7XOU-IQ74-BJ5L-WP7G", new PatriciaTreeNode(3000, 0));


        accountStateModels.add(new AccountStateModel(new AccountStateObject(address1,2),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address2,2),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address3,2),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address4,2),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address5,2),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address6,2),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address7,2),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address8,2),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address9,2),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address10,2),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address11,2),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address12,2),3000,3000));

        accountStateModels.add(new AccountStateModel(new AccountStateObject("ADR-GBIV-HG2J-27P5-BNVN-MLN6-DL5V-M3YZ-PKEJ-CFFG-FK4L",2),1000,1000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject("ADR-GBZX-XXCW-LWJC-J7RZ-Q6BJ-RFBA-J5WU-NBAG-4RL7-7G6Z",2),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject("ADR-GD3G-DK4I-DKM2-IQSB-KBWL-HWRV-BBQA-MUAS-MGXA-5QPP",2),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject("ADR-GC2I-WBAW-IKJE-BWFC-ML6T-BNOC-7XOU-IQ74-BJ5L-WP7G",2),3000,3000));

        TreeFactory.getMemoryTree(3).store(address1, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(3).store(address2, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(3).store(address3, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(3).store(address4, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(3).store(address5, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(3).store(address6, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(3).store(address7, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(3).store(address8, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(3).store(address9, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(3).store(address10, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(3).store(address11, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(3).store(address12, new PatriciaTreeNode(3000, 0));
        TreeFactory.getMemoryTree(3).store("ADR-GBIV-HG2J-27P5-BNVN-MLN6-DL5V-M3YZ-PKEJ-CFFG-FK4L", new PatriciaTreeNode(1000, 0));
        TreeFactory.getMemoryTree(3).store("ADR-GBZX-XXCW-LWJC-J7RZ-Q6BJ-RFBA-J5WU-NBAG-4RL7-7G6Z", new PatriciaTreeNode(4000, 0));
        TreeFactory.getMemoryTree(3).store("ADR-GD3G-DK4I-DKM2-IQSB-KBWL-HWRV-BBQA-MUAS-MGXA-5QPP", new PatriciaTreeNode(4000, 0));
        TreeFactory.getMemoryTree(3).store("ADR-GC2I-WBAW-IKJE-BWFC-ML6T-BNOC-7XOU-IQ74-BJ5L-WP7G", new PatriciaTreeNode(4000, 0));

        accountStateModels.add(new AccountStateModel(new AccountStateObject(address1,3),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address2,3),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address3,3),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address4,3),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address5,3),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address6,3),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address7,3),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address8,3),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address9,3),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address10,3),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address11,3),3000,3000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject(address12,3),3000,3000));

        accountStateModels.add(new AccountStateModel(new AccountStateObject("ADR-GBIV-HG2J-27P5-BNVN-MLN6-DL5V-M3YZ-PKEJ-CFFG-FK4L",3),1000,1000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject("ADR-GBZX-XXCW-LWJC-J7RZ-Q6BJ-RFBA-J5WU-NBAG-4RL7-7G6Z",3),4000,4000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject("ADR-GD3G-DK4I-DKM2-IQSB-KBWL-HWRV-BBQA-MUAS-MGXA-5QPP",3),4000,4000));
        accountStateModels.add(new AccountStateModel(new AccountStateObject("ADR-GC2I-WBAW-IKJE-BWFC-ML6T-BNOC-7XOU-IQ74-BJ5L-WP7G",3),4000,4000));


        accountService.saveAll(accountModels);
        accountStateService.saveAll(accountStateModels);

        kad1 = new KademliaData(new SecurityAuditProofs(address1, vk1, ecKeyPair1.getPublicKey(), signatureData1), new NettyConnectionInfo("192.168.1.106", KademliaConfiguration.PORT));
        kad2 = new KademliaData(new SecurityAuditProofs(address2, vk2, ecKeyPair2.getPublicKey(), signatureData2), new NettyConnectionInfo("192.168.1.113", KademliaConfiguration.PORT));
        kad3 = new KademliaData(new SecurityAuditProofs(address3, vk3, ecKeyPair3.getPublicKey(), signatureData3), new NettyConnectionInfo("192.168.1.116", KademliaConfiguration.PORT));
        kad4 = new KademliaData(new SecurityAuditProofs(address4, vk4, ecKeyPair4.getPublicKey(), signatureData4), new NettyConnectionInfo("192.168.1.110", KademliaConfiguration.PORT));
        kad5 = new KademliaData(new SecurityAuditProofs(address5, vk5, ecKeyPair5.getPublicKey(), signatureData5), new NettyConnectionInfo("192.168.1.112", KademliaConfiguration.PORT));
        kad6 = new KademliaData(new SecurityAuditProofs(address6, vk6, ecKeyPair6.getPublicKey(), signatureData6), new NettyConnectionInfo("192.168.1.115", KademliaConfiguration.PORT));
        CommitteeBlock committeeBlock = new CommitteeBlock();
        committeeBlock.getHeaderData().setTimestamp("2022-11-18 15:01:29.304");
        committeeBlock.getStructureMap().get(0).put(vk1, "192.168.1.106");
        committeeBlock.getStructureMap().get(0).put(vk2, "192.168.1.113");
        committeeBlock.getStructureMap().get(0).put(vk3, "192.168.1.116");
        //committeeBlock.getStructureMap().get(1).put(vk4, "192.168.1.110");
        // committeeBlock.getStructureMap().get(1).put(vk5, "192.168.1.112");
        // committeeBlock.getStructureMap().get(1).put(vk6, "192.168.1.115");


        committeeBlock.getStakingMap().put(new StakingData(1, 10.0), kad1);
        committeeBlock.getStakingMap().put(new StakingData(2, 11.0), kad2);
        committeeBlock.getStakingMap().put(new StakingData(3, 151.0), kad3);
        // committeeBlock.getStakingMap().put(new StakingData(4, 16.0), kad4);
        // committeeBlock.getStakingMap().put(new StakingData(5, 271.0), kad5);
        // committeeBlock.getStakingMap().put(new StakingData(6, 281.0), kad6);

        CachedLatestBlocks.getInstance().setCommitteeBlock(committeeBlock);

        CachedLatestBlocks.getInstance().getCommitteeBlock().setDifficulty(112);
        CachedLatestBlocks.getInstance().getCommitteeBlock().setHash("hash");
        CachedLatestBlocks.getInstance().getCommitteeBlock().setGeneration(0);
        CachedLatestBlocks.getInstance().getCommitteeBlock().setHeight(0);
        committee_database.save(String.valueOf(CachedLatestBlocks.getInstance().getCommitteeBlock().getHeight()), CachedLatestBlocks.getInstance().getCommitteeBlock());


        TransactionBlock prevblock = new TransactionBlock();
        prevblock.setHeight(1);
        prevblock.setHash("hash");
        prevblock.getHeaderData().setTimestamp(GetTime.GetTimeStampInString());
        prevblock.setTransactionProposer(vk1.toRaw());
        prevblock.setLeaderPublicKey(vk1);
        CachedLatestBlocks.getInstance().setTransactionBlock(prevblock);


        TransactionBlock TransactionBlockZone2 = new TransactionBlock();
        TransactionBlockZone2.setHeight(1);
        TransactionBlockZone2.setHash("TransactionBlockZone0");
        TransactionBlockZone2.getHeaderData().setTimestamp(GetTime.GetTimeStampInString());
        Thread.sleep(200);
        TransactionBlock TransactionBlockZone3 = new TransactionBlock();
        TransactionBlockZone3.setHeight(1);
        TransactionBlockZone3.setHash("TransactionBlockZone0");
        TransactionBlockZone3.getHeaderData().setTimestamp(GetTime.GetTimeStampInString());

        IDatabase<String, TransactionBlock> TransactionBlockZone0 = new DatabaseFactory(String.class, TransactionBlock.class).getDatabase(DatabaseType.ROCKS_DB, ZoneDatabaseFactory.getZoneInstance(0));
        IDatabase<String, TransactionBlock> Zone2TransactionDatabase = new DatabaseFactory(String.class, TransactionBlock.class).getDatabase(DatabaseType.ROCKS_DB, ZoneDatabaseFactory.getZoneInstance(2));
        IDatabase<String, TransactionBlock> Zone3TransactionDatabase = new DatabaseFactory(String.class, TransactionBlock.class).getDatabase(DatabaseType.ROCKS_DB, ZoneDatabaseFactory.getZoneInstance(3));
        TransactionBlockZone0.save(String.valueOf(CachedLatestBlocks.getInstance().getTransactionBlock().getHeight()), CachedLatestBlocks.getInstance().getTransactionBlock());
        Zone2TransactionDatabase.save("1", TransactionBlockZone2);
        Zone3TransactionDatabase.save("1", TransactionBlockZone3);

        IDatabase<String, byte[]> patricia_tree0 = new DatabaseFactory(String.class, byte[].class).getDatabase(DatabaseType.ROCKS_DB, PatriciaTreeInstance.PATRICIA_TREE_INSTANCE_0);
        IDatabase<String, byte[]> patricia_tree1 = new DatabaseFactory(String.class, byte[].class).getDatabase(DatabaseType.ROCKS_DB, PatriciaTreeInstance.PATRICIA_TREE_INSTANCE_1);
        IDatabase<String, byte[]> patricia_tree2 = new DatabaseFactory(String.class, byte[].class).getDatabase(DatabaseType.ROCKS_DB, PatriciaTreeInstance.PATRICIA_TREE_INSTANCE_2);
        IDatabase<String, byte[]> patricia_tree3 = new DatabaseFactory(String.class, byte[].class).getDatabase(DatabaseType.ROCKS_DB, PatriciaTreeInstance.PATRICIA_TREE_INSTANCE_3);


        patricia_tree0.save(String.valueOf(0), patricia_tree_wrapper.encode(TreeFactory.getMemoryTree(0)));
        patricia_tree1.save(String.valueOf(1),patricia_tree_wrapper.encode(TreeFactory.getMemoryTree(1)));
        patricia_tree2.save(String.valueOf(2),patricia_tree_wrapper.encode(TreeFactory.getMemoryTree(2)));
        patricia_tree3.save(String.valueOf(3),patricia_tree_wrapper.encode(TreeFactory.getMemoryTree(3)));

        CachedEventLoop.getInstance().start();
    }

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {

    }

}
