package org.davex.chainmaker.config;


import lombok.extern.slf4j.Slf4j;
import org.chainmaker.sdk.ChainClient;
import org.chainmaker.sdk.ChainManager;
import org.chainmaker.sdk.User;
import org.chainmaker.sdk.config.NodeConfig;
import org.chainmaker.sdk.config.SdkConfig;
import org.chainmaker.sdk.utils.FileUtils;
import org.chainmaker.sdk.utils.UtilsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Slf4j
public class ChainClientConfig {

    @Value("${chainmaker.sdk.user.key-path}")
    private String userKeyPath;

    @Value("${chainmaker.sdk.user.cert-path}")
    private String userCertPath;

    @Value("${chainmaker.sdk.user.tls-key-path}")
    private String tlsKeyPath;

    @Value("${chainmaker.sdk.user.tls-cert-path}")
    private String tlsCertPath;

    @Value("${chainmaker.sdk.org-id}")
    private String orgId;

    @Value("${chainmaker.sdk.config-path}")
    private String sdkConfigPath;

    @Bean
    public ChainClient chainClient() throws Exception {
        log.info("开始初始化 ChainClient, sdkConfigPath: {}", sdkConfigPath);

        InputStream in = null;
        try {
            Representer representer = new Representer(new DumperOptions());
            representer.getPropertyUtils().setSkipMissingProperties(true);
            Yaml yaml = new Yaml(representer);

            // 使用当前类的类加载器加载资源
            in = Thread.currentThread().getContextClassLoader().getResourceAsStream(sdkConfigPath);
            if (in == null) {
                throw new FileNotFoundException("找不到配置文件: " + sdkConfigPath);
            }

            // 加载配置
            SdkConfig sdkConfig = yaml.loadAs(in, SdkConfig.class);
            if (sdkConfig == null) {
                throw new IllegalStateException("配置文件解析失败");
            }

            // 处理节点配置
            for (NodeConfig nodeConfig : sdkConfig.getChainClient().getNodes()) {
                List<byte[]> tlsCaCertList = new ArrayList<>();
                if (nodeConfig.getTrustRootPaths() != null) {
                    for (String rootPath : nodeConfig.getTrustRootPaths()) {
                        log.info("处理证书路径: {}", rootPath);
                        List<String> filePathList = FileUtils.getFilesByPath(rootPath);
                        for (String filePath : filePathList) {
                            log.info("加载证书文件: {}", filePath);
                            tlsCaCertList.add(FileUtils.getFileBytes(filePath));
                        }
                    }
                }

                byte[][] tlsCaCerts = new byte[tlsCaCertList.size()][];
                tlsCaCertList.toArray(tlsCaCerts);
                nodeConfig.setTrustRootBytes(tlsCaCerts);
            }

            // 初始化 ChainClient
            ChainManager chainManager = ChainManager.getInstance();
            ChainClient chainClient = chainManager.getChainClient(sdkConfig.getChainClient().getChainId());

            if (chainClient == null) {
                log.info("创建新的 ChainClient 实例");
                chainClient = chainManager.createChainClient(sdkConfig);
            }

            if (chainClient == null) {
                throw new IllegalStateException("ChainClient 创建失败");
            }

            log.info("ChainClient 初始化成功");
            return chainClient;

        } catch (Exception e) {
            log.error("ChainClient 初始化失败", e);
            throw new RuntimeException("ChainClient 初始化失败: " + e.getMessage(), e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    log.warn("关闭配置文件流时发生错误", e);
                }
            }
        }
    }
}
