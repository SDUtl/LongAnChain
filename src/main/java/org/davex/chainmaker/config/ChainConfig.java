package org.davex.chainmaker.config;

import org.chainmaker.pb.config.ChainConfigOuterClass;
import org.chainmaker.sdk.ChainClient;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChainConfig {
    public static void getChainConfig(ChainClient chainClient) {
        ChainConfigOuterClass.ChainConfig chainConfig = null;
        try {
            chainConfig = chainClient.getChainConfig(20000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert chainConfig != null;
        System.out.println(chainConfig.toString());
    }
}
