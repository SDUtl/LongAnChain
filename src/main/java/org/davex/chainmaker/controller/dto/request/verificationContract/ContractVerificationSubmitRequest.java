package org.davex.chainmaker.controller.dto.request.verificationContract;

import lombok.Data;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import java.util.UUID;

import org.davex.chainmaker.util.JsonFileUtil;
import org.davex.chainmaker.util.PersistentKeyUtils;

@Data
public class ContractVerificationSubmitRequest {
    private String requestID;
    private String timeStamp; //单位都是ms
    private String fileDescription;
    private String sharingSettingHash;
    private String certificateID;
    private String publicKey;
    private String requestSignature;
    private String requestMsgHash;

    // 生成请求的示例数据
    public static ContractVerificationSubmitRequest generateRequest(String fileDescription, PrivateKey privateKey) throws Exception {
        ContractVerificationSubmitRequest request = new ContractVerificationSubmitRequest();


        // 生成唯一的 requestID 和 certificateID
        request.setRequestID(UUID.randomUUID().toString());
        request.setCertificateID(UUID.randomUUID().toString());

        // 设置当前时间戳
        request.setTimeStamp(String.valueOf(System.currentTimeMillis()));

        // 设置文件描述
        request.setFileDescription(fileDescription);

        // 假设 sharingSettingHash 是一个静态哈希值（根据需求替换为真实数据）
        request.setSharingSettingHash(generateHash("SharingSettingExampleData"));

        // 生成请求消息的哈希值
        String messageToHash = generateHash("LongGe");
        request.setRequestMsgHash(messageToHash);

        // 使用私钥生成签名
        request.setRequestSignature(generateSignature(messageToHash, privateKey));

        return request;
    }

    // 使用 SHA-256 生成 Base64 编码的哈希值
    private static String generateHash(String data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }

    // 使用指定的 SHA256withRSA 算法生成签名
    private static String generateSignature(String data, PrivateKey privateKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        byte[] signedData = signature.sign();
        return Base64.getEncoder().encodeToString(signedData);
    }

    // 验证签名的方法
    public static boolean verifySignature(String data, String signatureStr, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        byte[] signatureBytes = Base64.getDecoder().decode(signatureStr);
        return signature.verify(signatureBytes);
    }

    public static void main(String[] args) {
        try {
            // 生成一个 RSA 密钥对
            KeyPair keyPair = PersistentKeyUtils.initializeKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            PublicKey publicKey = keyPair.getPublic();

            // 生成示例请求数据
            ContractVerificationSubmitRequest request = generateRequest("This is a file description for contract verification.", privateKey);

            // 输出生成的请求信息
            System.out.println("Generated Request:");
            System.out.println("Request ID: " + request.getRequestID());
            System.out.println("Time Stamp: " + request.getTimeStamp());
            System.out.println("File Description: " + request.getFileDescription());
            System.out.println("Sharing Setting Hash: " + request.getSharingSettingHash());
            request.setCertificateID("9247c912-003b-4e39-b781-cfa07050a3ac");
            System.out.println("Certificate ID: " + request.getCertificateID());
            System.out.println("Request Signature: " + request.getRequestSignature());
            System.out.println("Request Message Hash: " + request.getRequestMsgHash());

            String filePath = "output.json";
            try {
                JsonFileUtil.appendToJsonFile(filePath, request);
                System.out.println("Data appended to " + filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // 使用与签名时相同的原始数据进行验证
            String messageToHash = generateHash("LongGe");
            System.out.println("messageHash" + messageToHash);
            String requestSignature = "JeY6+p+ed41BWk4+SRKLxXUhYqiwKJylmLlES6NuCAVxFwDmr63YBK00oWWnS42v40i6gw4qb0/m78b2bVzDcLtjkK1FVWArYcILTyhBuWRN43Lr4KSFh/7JU0JqDJ7IUiPuIAvtlqS0ROlmD0Axg5q3eFwnrRw1XzVDlM+Z1IscNKNaIb4OBFRHl2Xh/QdoWl91+6K7hb2lFEBvyKJRWk/XN0VFOdRB4YTc9BfX1JgZqym4NPEUEIl5E7xpD5hWFOwi9mtpBunecP5EqQgWLSUjpC9ptKk5AyCVUy7rE95XhWmBMH4IkfMQX1i2M+pWExT1w6/5bFhFTs46zeHA1g==";

            boolean isValid = verifySignature(messageToHash, requestSignature, publicKey);
            System.out.println("Signature verification result: " + isValid);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
