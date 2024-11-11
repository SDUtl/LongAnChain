package org.davex.chainmaker.util;

import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class PublicKeyEncodingExample {
    // 生成 RSA 密钥对
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);
        return keyPairGen.generateKeyPair();
    }

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

    // 签名数据
    public static String signData(String data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes());
        byte[] signedData = signature.sign();
        return Base64.getEncoder().encodeToString(signedData);
    }

    public static boolean verifySignature(String data, String signatureStr, String publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        PublicKey  publicKey1 = PemUtil.decodePublicKeyFromString(publicKey);
        signature.initVerify(publicKey1);
        signature.update(data.getBytes());
        byte[] signatureBytes = Base64.getDecoder().decode(signatureStr);
        return signature.verify(signatureBytes);
    }
    // 验证签名
    public static boolean verifySignature(String data, String signatureStr, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(data.getBytes());
        byte[] signatureBytes = Base64.getDecoder().decode(signatureStr);
        return signature.verify(signatureBytes);
    }

    public static void main(String[] args) {
        try {
            // 生成密钥对
            KeyPair keyPair = generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            // 将公钥编码为字符串
            String publicKeyStr = encodePublicKeyToString(publicKey);
            System.out.println("Encoded Public Key:\n" + publicKeyStr);

            // 解码字符串为 PublicKey 对象
            PublicKey decodedPublicKey = decodePublicKeyFromString(publicKeyStr);

            // 签名数据
            String data = "LongGe";
            String signature = signData(data, privateKey);
            System.out.println("Generated Signature: " + signature);

            // 使用解码后的公钥验证签名
            boolean isValid = verifySignature(data, signature, decodedPublicKey);
            System.out.println("Signature verification result: " + isValid);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
