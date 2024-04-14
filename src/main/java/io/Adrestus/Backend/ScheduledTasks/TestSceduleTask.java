package io.Adrestus.Backend.ScheduledTasks;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.Adrestus.Backend.Config.APIConfiguration;
import io.Adrestus.Backend.DTO.LimitBlockDetailsDTO;
import io.Adrestus.Backend.DTO.LimitTransactionsDetailsDTO;
import io.Adrestus.Backend.DTO.TransactionDetailsDTO;
import io.Adrestus.Backend.Service.AccountService;
import io.Adrestus.Backend.Service.AccountStateService;
import io.Adrestus.Backend.Service.BlockService;
import io.Adrestus.Backend.Service.TransactionService;
import io.Adrestus.Backend.Util.JsonConvertUtil;
import io.Adrestus.Backend.model.*;
import io.Adrestus.config.AdrestusConfiguration;
import io.Adrestus.core.StatusType;
import io.Adrestus.core.TransactionType;
import io.Adrestus.crypto.HashUtil;
import io.Adrestus.crypto.WalletAddress;
import io.Adrestus.crypto.bls.model.BLSPrivateKey;
import io.Adrestus.crypto.bls.model.BLSPublicKey;
import io.Adrestus.crypto.elliptic.ECDSASign;
import io.Adrestus.crypto.elliptic.ECDSASignatureData;
import io.Adrestus.crypto.elliptic.ECKeyPair;
import io.Adrestus.crypto.elliptic.Keys;
import io.Adrestus.crypto.mnemonic.Mnemonic;
import io.Adrestus.crypto.mnemonic.MnemonicException;
import io.Adrestus.crypto.mnemonic.Security;
import io.Adrestus.crypto.mnemonic.WordList;
import org.apache.commons.codec.binary.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.StringWriter;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.TreeSet;

@Component
public class TestSceduleTask {

    @Autowired
    private SimpMessagingTemplate template;
    private int counter;
    private static BLSPrivateKey sk1;
    private static BLSPublicKey vk1;

    public TestSceduleTask() {
        counter = 0;
    }

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


    @Scheduled(fixedRate = APIConfiguration.TRANSACTION_TEST_BLOCK_RATE)
    public void test() throws InterruptedException, MnemonicException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, NoSuchProviderException, IOException {

        TreeSet<String> addresses = new TreeSet<>();
        ArrayList<TransactionModel> transactionModels = new ArrayList<>();
        ArrayList<AccountModel> accountModels = new ArrayList<>();
        ArrayList<AccountStateModel> accountStateModels = new ArrayList<>();

        int version = 0x00;
        sk1 = new BLSPrivateKey(1);
        vk1 = new BLSPublicKey(sk1);
        ECDSASign ecdsaSign = new ECDSASign();

        char[] mnemonic1 = "sample sail jungle learn general promote task puppy own conduct green affair".toCharArray();
        char[] mnemonic2 = "photo monitor cushion indicate civil witness orchard estate online favorite sustain extend".toCharArray();
        char[] mnemonic3 = "initial car bulb nature animal honey learn awful grit arrow phrase entire".toCharArray();
        char[] mnemonic4 = "enrich pulse twin version inject horror village aunt brief magnet blush else".toCharArray();
        char[] passphrase = "12345678".toCharArray();

        Mnemonic mnem = new Mnemonic(Security.NORMAL, WordList.ENGLISH);
        byte[] key1 = mnem.createSeed(mnemonic1, passphrase);
        byte[] key2 = mnem.createSeed(mnemonic2, passphrase);
        byte[] key3 = mnem.createSeed(mnemonic3, passphrase);
        byte[] key4 = mnem.createSeed(mnemonic4, passphrase);

        SecureRandom random = SecureRandom.getInstance(AdrestusConfiguration.ALGORITHM, AdrestusConfiguration.PROVIDER);
        random.setSeed(key1);
        ECKeyPair ecKeyPair1 = Keys.createEcKeyPair(random);
        random.setSeed(key2);
        ECKeyPair ecKeyPair2 = Keys.createEcKeyPair(random);
        random.setSeed(key3);
        ECKeyPair ecKeyPair3 = Keys.createEcKeyPair(random);
        random.setSeed(key4);
        ECKeyPair ecKeyPair4 = Keys.createEcKeyPair(random);
        String adddress1 = WalletAddress.generate_address((byte) version, ecKeyPair1.getPublicKey());
        String adddress2 = WalletAddress.generate_address((byte) version, ecKeyPair2.getPublicKey());
        String adddress3 = WalletAddress.generate_address((byte) version, ecKeyPair3.getPublicKey());
        String adddress4 = "ADR-GC2I-WBAW-IKJE-BWFC-ML6T-BNOC-7XOU-IQ74-BJ5L-WP7G";

        BlockModel blockModel = new BlockModel();
        blockModel.setPreviousHash("blockhash" + (counter - 1));
        blockModel.setBlockhash(HashUtil.sha256("blockhash" + counter));
        blockModel.setVersion(0x00);
        blockModel.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());
        blockModel.setHeight(counter);
        blockModel.setGeneration(counter);
        blockModel.setViewID(counter);
        blockModel.setZone(1);
        blockModel.setTransactionProposer(vk1.toRaw());
        blockModel.setLeaderPublicKey(vk1.toString());
        blockModel.setSize(100 + counter);
        blockModel.setMerkleRoot(HashUtil.sha256("Merklee" + counter));
        blockModel.setPatriciaMerkleRoot(HashUtil.sha256("Patricia" + counter));

        TransactionModel transactionModel = new TransactionModel();
        transactionModel.setFrom(adddress1);
        transactionModel.setTo(adddress2);
        transactionModel.setTransactionhash(HashUtil.sha256("trhash" + counter));
        transactionModel.setBlockModel(blockModel);
        transactionModel.setStatus(StatusType.PENDING);
        transactionModel.setType(TransactionType.REGULAR);
        transactionModel.setZoneFrom(1);
        transactionModel.setZoneTo(2);
        transactionModel.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());
        transactionModel.setAmount(counter + 100);
        transactionModel.setAmountWithTransactionFee((counter + 100) * ((double) 10 / 100));
        transactionModel.setNonce(counter);
        transactionModel.setPosition(counter);
        transactionModel.setXAxis(ramdonBigInteger().toString());
        transactionModel.setYAxis(ramdonBigInteger().toString());
        transactionModel.setV((byte) 1);
        ECDSASignatureData signatureData = ecdsaSign.secp256SignMessage(org.spongycastle.util.encoders.Hex.decode(transactionModel.getTransactionhash()), ecKeyPair1);
        transactionModel.setR(Hex.encodeHexString(signatureData.getR()));
        transactionModel.setS(Hex.encodeHexString(signatureData.getS()));
        transactionModel.setPub(ecKeyPair1.getPublicKey().toString());

        TransactionModel transactionModel1 = new TransactionModel();
        transactionModel1.setFrom(adddress2);
        transactionModel1.setTo(adddress1);
        transactionModel1.setTransactionhash(HashUtil.sha256("trhash" + counter + "a"));
        transactionModel1.setBlockModel(blockModel);
        transactionModel1.setStatus(StatusType.PENDING);
        transactionModel1.setType(TransactionType.REGULAR);
        transactionModel1.setZoneFrom(1);
        transactionModel1.setZoneTo(2);
        transactionModel1.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());
        transactionModel1.setAmount(counter + 100);
        transactionModel1.setAmountWithTransactionFee((counter + 100) * ((double) 10 / 100));
        transactionModel1.setNonce(counter);
        transactionModel1.setPosition(counter);
        transactionModel1.setXAxis(ramdonBigInteger().toString());
        transactionModel1.setYAxis(ramdonBigInteger().toString());
        transactionModel1.setV((byte) 1);
        ECDSASignatureData signatureData1 = ecdsaSign.secp256SignMessage(org.spongycastle.util.encoders.Hex.decode(transactionModel.getTransactionhash()), ecKeyPair2);
        transactionModel1.setR(Hex.encodeHexString(signatureData1.getR()));
        transactionModel1.setS(Hex.encodeHexString(signatureData1.getS()));
        transactionModel1.setPub(ecKeyPair2.getPublicKey().toString());

        TransactionModel transactionModel3 = new TransactionModel();
        transactionModel3.setFrom(adddress1);
        transactionModel3.setTo(adddress3);
        transactionModel3.setTransactionhash(HashUtil.sha256("trhash" + counter + "b"));
        transactionModel3.setBlockModel(blockModel);
        transactionModel3.setStatus(StatusType.PENDING);
        transactionModel3.setType(TransactionType.REGULAR);
        transactionModel3.setZoneFrom(1);
        transactionModel3.setZoneTo(2);
        transactionModel3.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());
        transactionModel3.setAmount(counter + 100);
        transactionModel3.setAmountWithTransactionFee((counter + 100) * ((double) 10 / 100));
        transactionModel3.setNonce(counter);
        transactionModel3.setPosition(counter);
        transactionModel3.setXAxis(ramdonBigInteger().toString());
        transactionModel3.setYAxis(ramdonBigInteger().toString());
        transactionModel3.setV((byte) 1);
        ECDSASignatureData signatureData3 = ecdsaSign.secp256SignMessage(org.spongycastle.util.encoders.Hex.decode(transactionModel.getTransactionhash()), ecKeyPair1);
        transactionModel3.setR(Hex.encodeHexString(signatureData3.getR()));
        transactionModel3.setS(Hex.encodeHexString(signatureData3.getS()));
        transactionModel3.setPub(ecKeyPair1.getPublicKey().toString());

        TransactionModel transactionModel4 = new TransactionModel();
        transactionModel4.setFrom(adddress4);
        transactionModel4.setTo(adddress1);
        transactionModel4.setTransactionhash(HashUtil.sha256("trhash" + counter + "c"));
        transactionModel4.setBlockModel(blockModel);
        transactionModel4.setStatus(StatusType.PENDING);
        transactionModel4.setType(TransactionType.REGULAR);
        transactionModel4.setZoneFrom(1);
        transactionModel4.setZoneTo(2);
        transactionModel4.setTimestamp(new Timestamp(System.currentTimeMillis()).toString());
        transactionModel4.setAmount(counter + 100);
        transactionModel4.setAmountWithTransactionFee((counter + 100) * ((double) 10 / 100));
        transactionModel4.setNonce(counter);
        transactionModel4.setPosition(counter);
        transactionModel4.setXAxis(ramdonBigInteger().toString());
        transactionModel4.setYAxis(ramdonBigInteger().toString());
        transactionModel4.setV((byte) 1);
        ECDSASignatureData signatureData4 = ecdsaSign.secp256SignMessage(org.spongycastle.util.encoders.Hex.decode(transactionModel.getTransactionhash()), ecKeyPair4);
        transactionModel4.setR(Hex.encodeHexString(signatureData4.getR()));
        transactionModel4.setS(Hex.encodeHexString(signatureData4.getS()));
        transactionModel4.setPub(ecKeyPair4.getPublicKey().toString());

        transactionModels.add(transactionModel);
        transactionModels.add(transactionModel1);
        transactionModels.add(transactionModel3);
        transactionModels.add(transactionModel4);


        AccountModel accountModel = new AccountModel();
        accountModel.setTimestamp(new Timestamp(System.currentTimeMillis()));
        accountModel.setAddress(adddress1);
        accountModels.add(accountModel);

        for (int i = 0; i < 4; i++) {
            AccountStateModel accountStateModel = new AccountStateModel();
            accountStateModel.setBalance(100);
            accountStateModel.setStaked(50);
            accountStateModel.setAccountStateObject(new AccountStateObject(adddress1, i));
            accountStateModels.add(accountStateModel);
        }


        AccountModel accountModel1 = new AccountModel();
        accountModel1.setTimestamp(new Timestamp(System.currentTimeMillis()));
        accountModel1.setAddress(adddress2);
        accountModels.add(accountModel1);
        for (int i = 0; i < 4; i++) {
            AccountStateModel accountStateModel = new AccountStateModel();
            accountStateModel.setBalance(100);
            accountStateModel.setStaked(50);
            accountStateModel.setAccountStateObject(new AccountStateObject(adddress2, i));
            accountStateModels.add(accountStateModel);
        }
        AccountModel accountModel2 = new AccountModel();
        accountModel2.setTimestamp(new Timestamp(System.currentTimeMillis()));
        accountModel2.setAddress(adddress3);
        accountModels.add(accountModel2);

        for (int i = 0; i < 4; i++) {
            AccountStateModel accountStateModel = new AccountStateModel();
            accountStateModel.setBalance(100);
            accountStateModel.setStaked(50);
            accountStateModel.setAccountStateObject(new AccountStateObject(adddress3, i));
            accountStateModels.add(accountStateModel);
        }

        AccountModel accountModel3 = new AccountModel();
        accountModel3.setTimestamp(new Timestamp(System.currentTimeMillis()));
        accountModel3.setAddress(adddress4);
        accountModels.add(accountModel3);

        for (int i = 0; i < 3; i++) {
            AccountStateModel accountStateModel = new AccountStateModel();
            accountStateModel.setBalance(100);
            accountStateModel.setStaked(50);
            accountStateModel.setAccountStateObject(new AccountStateObject(adddress4, i));
            accountStateModels.add(accountStateModel);
        }

//        AccountModel md = accountService.findAccountByAddress(accountModel.getAddress());
//        if (md == null)
//            accountService.save(accountModel);
//            AccountStateModel md2 = accountStateService.findByAccountStateObjectAccountId(count - 1);
//            if (md2 == null) {
//                try {
//                    accountStateService.save(accountStateModel);
//                } catch (Exception e) {
//                    int g = 3;
//                }
//            }
        blockService.save(blockModel);
        transactionService.saveAll(transactionModels);
        accountService.saveAll(accountModels);
        addresses.add(adddress1);
        addresses.add(adddress2);
        addresses.add(adddress3);
        addresses.add(adddress4);
        accountStateService.saveAll(accountStateModels);

        ArrayList<String>trxHashes=new ArrayList<>();
        transactionModels.forEach(val->trxHashes.add(val.getTransactionhash()));
        LimitBlockDetailsDTO limitBlockDetailsDTO=this.blockService.findLimitBlockDetailsDTOByHash(blockModel.getBlockhash());
        List<LimitTransactionsDetailsDTO> limitTransactionsDetailsDTOS=this.transactionService.findLimitTransactionsDetailsByTransactionHash(trxHashes);
//        String jsonBlockModel= JsonConvertUtil.ObjtoString(limitBlockDetailsDTO);
//        String jsonTransactionModels= JsonConvertUtil.ArrayObjectToString(limitTransactionsDetailsDTOS);
        this.template.convertAndSend("/topic/blocks", limitBlockDetailsDTO);
        this.template.convertAndSend("/topic/transactions", limitTransactionsDetailsDTOS);

        //       int g = accountStateService.updateAccountSetBalanceForAddress(500, "from1", 0);
//        accountStateModel.setStaked(300);
//        accountStateService.save(accountStateModel);
        counter++;
    }

    public BigInteger ramdonBigInteger() {
        BigInteger maxLimit = new BigInteger("5000000000000");
        BigInteger minLimit = new BigInteger("25000000000");
        BigInteger bigInteger = maxLimit.subtract(minLimit);
        Random randNum = new Random();
        int len = maxLimit.bitLength();
        BigInteger res = new BigInteger(len, randNum);
        if (res.compareTo(minLimit) < 0)
            res = res.add(minLimit);
        if (res.compareTo(bigInteger) >= 0)
            res = res.mod(bigInteger).add(minLimit);
        return res;
    }
}
