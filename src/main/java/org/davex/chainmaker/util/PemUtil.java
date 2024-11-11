package org.davex.chainmaker.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class PemUtil {
    // 读取 PEM 文件并转换为字符串
    // 将公钥编码为字符串
    public static String encodePublicKeyToString(PublicKey publicKey) {
        return "-----BEGIN PUBLIC KEY-----\n" +
                Base64.getEncoder().encodeToString(publicKey.getEncoded()) +
                "\n-----END PUBLIC KEY-----";
    }

    // 解码字符串为 PublicKey 对象
    public static PublicKey decodePublicKeyFromString(String publicKeyStr) throws Exception {
        String base64Content = publicKeyStr
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", ""); // 去除所有换行符和空格

        byte[] decodedKey = Base64.getDecoder().decode(base64Content);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decodedKey);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }


}
