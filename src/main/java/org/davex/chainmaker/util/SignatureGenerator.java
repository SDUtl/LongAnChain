package org.davex.chainmaker.util;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

public class SignatureGenerator {

    /**
     * 使用 RSA 私钥和 SHA-256 生成签名
     *
     * @param data 要签名的数据
     * @param privateKey 用于签名的私钥
     * @return 签名的字节数组
     * @throws Exception
     */
    public static byte[] generateSignature(String data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        return signature.sign();  // 返回签名的字节数组
    }

    /**
     * 验证签名
     *
     * @param data 原始数据
     * @param signatureBytes 需要验证的签名字节数组
     * @param publicKey 公钥
     * @return 验证结果，true表示验证成功，false表示验证失败
     * @throws Exception
     */
    public static boolean verifySignature(String data, byte[] signatureBytes, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        return signature.verify(signatureBytes);  // 验证签名
    }

    /**
     * 生成测试用的 RSA 密钥对
     *
     * @return 密钥对 (包含公钥和私钥)
     * @throws NoSuchAlgorithmException
     */
    public static KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(2048);
        return keyPairGen.generateKeyPair();
    }

    public static void main(String[] args) {
        try {
            // 生成一个测试用的密钥对
            KeyPair keyPair = generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            // 要签名的数据
            String data = "This is the data to sign";

            // 生成签名的原始字节数组
            byte[] signatureBytes = generateSignature(data, privateKey);

            // 输出签名的 Base64 编码
            String base64Signature = Base64.getEncoder().encodeToString(signatureBytes);
            System.out.println("Signature (Base64): " + base64Signature);

            // 从 Base64 编码的签名字符串解码回字节数组
            byte[] decodedSignature = Base64.getDecoder().decode(base64Signature);

            // 使用公钥验证签名
            boolean isValid = verifySignature(data, decodedSignature, publicKey);
            System.out.println("Signature verification result: " + isValid);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

