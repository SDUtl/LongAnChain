package org.davex.chainmaker.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.*;
import java.util.Base64;

public class PersistentKeyUtils {
    private static final String PRIVATE_KEY_FILE = "private_key.pem";
    private static final String PUBLIC_KEY_FILE = "public_key.pem";

    // 初始化密钥对：如果密钥文件存在则加载，否则生成并保存
    public static KeyPair initializeKeyPair() throws Exception {
        if (Files.exists(Paths.get(PRIVATE_KEY_FILE)) && Files.exists(Paths.get(PUBLIC_KEY_FILE))) {
            // 从文件加载密钥对
            PrivateKey privateKey = loadPrivateKey(PRIVATE_KEY_FILE);
            PublicKey publicKey = loadPublicKey(PUBLIC_KEY_FILE);
            return new KeyPair(publicKey, privateKey);
        } else {
            // 生成并保存密钥对
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair pair = keyGen.generateKeyPair();

            // 保存密钥对到文件
            savePrivateKey(pair.getPrivate(), PRIVATE_KEY_FILE);
            savePublicKey(pair.getPublic(), PUBLIC_KEY_FILE);
            System.out.println("Keys generated and saved to files.");

            return pair;
        }
    }

    // 保存私钥到 PEM 文件
    private static void savePrivateKey(PrivateKey privateKey, String filename) throws  IOException {
        String privateKeyPEM = "-----BEGIN PRIVATE KEY-----\n" +
                Base64.getEncoder().encodeToString(privateKey.getEncoded()) +
                "\n-----END PRIVATE KEY-----";
        Files.write(Paths.get(filename), privateKeyPEM.getBytes());
    }

    // 保存公钥到 PEM 文件
    private static void savePublicKey(PublicKey publicKey, String filename) throws IOException {
        String publicKeyPEM = "-----BEGIN PUBLIC KEY-----\n" +
                Base64.getEncoder().encodeToString(publicKey.getEncoded()) +
                "\n-----END PUBLIC KEY-----";
        Files.write(Paths.get(filename), publicKeyPEM.getBytes());
    }

    // 从 PEM 文件加载私钥
    private static PrivateKey loadPrivateKey(String filename) throws Exception {
        String key = new String(Files.readAllBytes(Paths.get(filename)))
                .replaceAll("-----BEGIN PRIVATE KEY-----", "")
                .replaceAll("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] keyBytes = Base64.getDecoder().decode(key);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(spec);
    }

    // 从 PEM 文件加载公钥
    private static PublicKey loadPublicKey(String filename) throws Exception {
        String key = new String(Files.readAllBytes(Paths.get(filename)))
                .replaceAll("-----BEGIN PUBLIC KEY-----", "")
                .replaceAll("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] keyBytes = Base64.getDecoder().decode(key);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(spec);
    }

    public static PublicKey parseString2PublicKey(String publicKeyPEM) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKeyPEM);
        // 创建 X509EncodedKeySpec
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);

        // 使用 KeyFactory 生成 PublicKey 对象
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    public static void main(String[] args) {
        try {
            // 初始化密钥对（首次生成并保存，后续加载）
            KeyPair keyPair = initializeKeyPair();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
