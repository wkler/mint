package icu.zhangbotong.evm;

import java.math.BigDecimal;

public interface EvmMintProperties {

    /**
     * RPC节点
     */
    String rpcUrl = "";

    /**
     * 私钥
     */
    String privateKey = "";

    /**
     * 钱包密码
     */
    String passowrd = "";

    /**
     * 铭文json数据
     */
    String tokenJson = "";

    /**
     * gas倍数
     */
    BigDecimal increaseGas = new BigDecimal("1.2");
}
