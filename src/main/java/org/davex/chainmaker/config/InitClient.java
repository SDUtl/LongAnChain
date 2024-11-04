package org.davex.chainmaker.config;

import org.chainmaker.sdk.ChainClient;
import org.chainmaker.sdk.ChainManager;
import org.chainmaker.sdk.SdkException;
import org.chainmaker.sdk.User;
import org.chainmaker.sdk.config.NodeConfig;
import org.chainmaker.sdk.config.SdkConfig;
import org.chainmaker.sdk.utils.FileUtils;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.representer.Representer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class InitClient {
    static final String CLIENT1_KEY_PATH = "crypto-config/DavexOrg1/user/davexuser1/davexuser1.sign.key";
    static final String CLIENT1_CERT_PATH = "crypto-config/DavexOrg1/user/davexuser1/davexuser1.sign.crt";

    static String CLIENT1_TLS_KEY_PATH = "crypto-config/DavexOrg1/user/davexuser1/davexuser1.tls.key";
    static String CLIENT1_TLS_CERT_PATH = "crypto-config/DavexOrg1/user/davexuser1/davexuser1.tls.crt";

    static final String ORG_ID1 = "DavexOrg1";
    static String  SDK_CONFIG = "./sdk_config.yml";

    static ChainClient chainClient;
    static ChainManager chainManager;
    static User user;

    public static void initChainClientForPk() throws Exception{
        Representer representer = new Representer(new DumperOptions());
        representer.getPropertyUtils().setSkipMissingProperties(true);
        Yaml yaml = new Yaml(representer);
        InputStream in = InitClient.class.getClassLoader().getResourceAsStream(SDK_CONFIG);

        SdkConfig sdkConfig;
        sdkConfig = yaml.loadAs(in, SdkConfig.class);
        assert in != null;
        in.close();

        for (NodeConfig nodeConfig : sdkConfig.getChainClient().getNodes()){
            List<byte[]> tlsCaCertList = new ArrayList<>();
            if(nodeConfig.getTrustRootPaths() != null){
                for (String rootPath : nodeConfig.getTrustRootPaths()) {
                    List<String> filePathList = FileUtils.getFilesByPath(rootPath);
                    for (String filePath : filePathList) {
                        tlsCaCertList.add(FileUtils.getFileBytes(filePath));
                    }
                }
            }

            byte[][] tlsCaCerts = new byte[tlsCaCertList.size()][];
            tlsCaCertList.toArray(tlsCaCerts);
            nodeConfig.setTrustRootBytes(tlsCaCerts);
        }

        chainManager = ChainManager.getInstance();
        chainClient = chainManager.getChainClient(sdkConfig.getChainClient().getChainId());

        if (chainClient == null) {
            chainClient = chainManager.createChainClient(sdkConfig);
        }

        System.out.println("Init Client Success");
    }

    public static void initChainClientForCert() throws Exception{
        Yaml yaml = new Yaml();
        InputStream in = InitClient.class.getClassLoader().getResourceAsStream(SDK_CONFIG);

        SdkConfig sdkConfig;
        sdkConfig = yaml.loadAs(in, SdkConfig.class);
        assert in != null;
        in.close();

        for (NodeConfig nodeConfig : sdkConfig.getChainClient().getNodes()) {
            List<byte[]> tlsCaCertList = new ArrayList<>();
            if (nodeConfig.getTrustRootPaths() != null) {
                for (String rootPath : nodeConfig.getTrustRootPaths()) {
                    List<String> filePathList = FileUtils.getFilesByPath(rootPath);
                    for (String filePath : filePathList) {
                        tlsCaCertList.add(FileUtils.getFileBytes(filePath));
                    }
                }
            }
            byte[][] tlsCaCerts = new byte[tlsCaCertList.size()][];
            tlsCaCertList.toArray(tlsCaCerts);
            nodeConfig.setTrustRootBytes(tlsCaCerts);
        }

        chainManager = ChainManager.getInstance();
        chainClient = chainManager.getChainClient(sdkConfig.getChainClient().getChainId());

        if (chainClient == null) {
            chainClient = chainManager.createChainClient(sdkConfig);
        }

        user = new User(ORG_ID1, FileUtils.getResourceFileBytes(CLIENT1_KEY_PATH),
                FileUtils.getResourceFileBytes(CLIENT1_CERT_PATH),
                FileUtils.getResourceFileBytes(CLIENT1_TLS_KEY_PATH),
                FileUtils.getResourceFileBytes(CLIENT1_TLS_CERT_PATH), false);
    }
}
