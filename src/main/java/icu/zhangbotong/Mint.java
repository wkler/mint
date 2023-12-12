package icu.zhangbotong;

import icu.zhangbotong.evm.EvmMint;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

import java.util.concurrent.CompletableFuture;

public class Mint {
    public static void main(String[] args) throws Exception {
        EvmMint evmMint = new EvmMint();
        CompletableFuture<EthSendTransaction> ethSendTransactionCompletableFuture = evmMint.sendTransaction();
    }
}
