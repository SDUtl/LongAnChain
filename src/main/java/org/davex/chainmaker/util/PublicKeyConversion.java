package org.davex.chainmaker.util;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PublicKey;
import java.util.Base64;

public class PublicKeyConversion {
    public static void main(String[] args) {
        try {
            // 生成公私钥对
            KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
            keyPairGen.initialize(2048);
            KeyPair keyPair = keyPairGen.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();

            // 将公钥编码为 DER 格式的字节数组
            byte[] publicKeyDER = publicKey.getEncoded();

            // 将字节数组转换为 Base64 编码的字符串
            String publicKeyString = Base64.getEncoder().encodeToString(publicKeyDER);
            System.out.println("Base64 编码后的公钥字符串:");
            System.out.println(publicKeyString);

            // 将 Base64 编码的字符串还原为字节数组
            byte[] restoredPublicKeyDER = Base64.getDecoder().decode(publicKeyString);

            // 验证还原后的字节数组与原始数组是否一致
            boolean isEqual = java.util.Arrays.equals(publicKeyDER, restoredPublicKeyDER);
            System.out.println("转换前后是否一致: " + isEqual);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
