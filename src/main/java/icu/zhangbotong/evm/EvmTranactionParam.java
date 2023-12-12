package icu.zhangbotong.evm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.web3j.protocol.core.methods.request.Transaction;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Accessors(chain = true)
public class EvmTranactionParam {

    private String wallAddress;

    private BigInteger nonce;

    private BigInteger gas;

    private BigInteger gasLimit;


    public static EvmTranactionParam buildWithWallAddress(String wallAddress) {
        EvmTranactionParam evmTranactionParam = new EvmTranactionParam();
        evmTranactionParam.setWallAddress(wallAddress);
        return evmTranactionParam;
    }


    public Transaction toEvmTransaction(String data){
       return new Transaction(
                wallAddress,
                nonce,
                gas,
                gasLimit,
                wallAddress,
                BigInteger.ZERO,
                data
        );
    }
}
