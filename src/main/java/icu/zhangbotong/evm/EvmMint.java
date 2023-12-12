package icu.zhangbotong.evm;

import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.Transaction;
import org.web3j.protocol.core.methods.response.EthEstimateGas;
import org.web3j.protocol.core.methods.response.EthGasPrice;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.CompletableFuture;

public class EvmMint {

    private Web3j web3j;

    private String walletAddress;

    public EvmMint(){
        web3j = Web3j.build(new HttpService(EvmMintProperties.rpcUrl));
        // 创建钱包
        BigInteger privateKeyAsBigInt = Numeric.toBigInt(EvmMintProperties.privateKey);
        ECKeyPair ecKeyPair = ECKeyPair.create(privateKeyAsBigInt);
        WalletFile walletFile = null;
        try {
            walletFile = Wallet.createLight(EvmMintProperties.passowrd, ecKeyPair);
        } catch (CipherException e) {
            throw new RuntimeException(e);
        }
        walletAddress = Numeric.prependHexPrefix(walletFile.getAddress());
    }


    public CompletableFuture<EthSendTransaction> sendTransaction(){
        String data = Numeric.prependHexPrefix(EvmMintProperties.tokenJson);
        EvmTranactionParam evmTranactionParam = EvmTranactionParam.buildWithWallAddress(walletAddress);
        return getCurrentNonce(walletAddress)
                .thenApply(evmTranactionParam::setNonce)
                .thenCompose(next -> getCurrentGas())
                .thenApply(gas -> {
                    BigInteger increaseGas = EvmMintProperties.increaseGas.multiply(new BigDecimal(100)).toBigInteger();
                    BigInteger increaseGasNext = gas.divide(gas.divide(new BigInteger("100"))).multiply(increaseGas);
                    evmTranactionParam.setGas(increaseGasNext);
                    return evmTranactionParam;
                })
                .thenCompose(next -> getCurrentGasLimit(evmTranactionParam.toEvmTransaction(data)).thenApply(evmTranactionParam::setGasLimit))
                .thenCompose(next -> web3j.ethSendTransaction(next.toEvmTransaction(data)).sendAsync())
                .exceptionally(throwable -> {
                    throwable.printStackTrace();
                    return null;
                })
       ;
    }

    private CompletableFuture<BigInteger> getCurrentNonce(String walletAddress){
        return web3j.ethGetTransactionCount(walletAddress, DefaultBlockParameterName.LATEST)
                .sendAsync()
                .thenApply(EthGetTransactionCount::getTransactionCount);
    }

    private CompletableFuture<BigInteger> getCurrentGas(){
        return web3j.ethGasPrice().sendAsync()
                .thenApply(EthGasPrice::getGasPrice);
    }

    private CompletableFuture<BigInteger> getCurrentGasLimit(Transaction transaction){
        return web3j.ethEstimateGas(transaction).sendAsync().thenApply(EthEstimateGas::getAmountUsed);
    }

}
