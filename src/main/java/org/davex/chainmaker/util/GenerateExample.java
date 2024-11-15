package org.davex.chainmaker.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.davex.chainmaker.controller.dto.request.verificationContract.ContractVerificationSubmitRequest;
import org.davex.chainmaker.controller.dto.request.verificationContract.ContractVerificationSubmitResponseRequest;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import static org.davex.chainmaker.controller.dto.request.verificationContract.ContractVerificationSubmitRequest.generateRequest;
import static org.davex.chainmaker.controller.dto.request.verificationContract.ContractVerificationSubmitResponseRequest.generateResponse;

//生成测试样例
public class GenerateExample {
    public static void main(String[] args) throws Exception {
        // 生成一个 RSA 密钥对
        KeyPair keyPair = PersistentKeyUtils.initializeKeyPair();
        PrivateKey privateKey = keyPair.getPrivate();
        PublicKey publicKey = keyPair.getPublic();


        // 生成 公钥信息
        byte[] publicKeyDER = publicKey.getEncoded();
        String publicKeyString = Base64.getEncoder().encodeToString(publicKeyDER);
        System.out.println(publicKeyString);

        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyString);

        // 创建 X509EncodedKeySpec
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);

        // 使用 KeyFactory 生成 PublicKey 对象
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey pk2 = keyFactory.generatePublic(keySpec);


        //从这里开始测试

        byte[] pk2DER = pk2.getEncoded();
        String pk2String = Base64.getEncoder().encodeToString(pk2DER);
        System.out.println(pk2String);

        // 生成示例请求数据
        ContractVerificationSubmitRequest request = generateRequest("This is a file description for contract verification.", privateKey);
        request.setCertificateID("9247c912-003b-4e39-b781-cfa07050a3ac");
        request.setPublicKey(publicKeyString);

        boolean verified = PublicKeyEncodingExample.verifySignature(request.getRequestMsgHash(), request.getRequestSignature(), publicKey);
        if (verified){
            System.out.println("Signature verified");
        }else {
            System.out.println("Signature not verified");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);
        System.out.println(json);

        //写入output.json 文件
        String filePath = "output.json";
        try {
            JsonFileUtil.appendToJsonFile(filePath, request);
            System.out.println("Data appended to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 生成示例响应数据
        String requestHash = request.getSharingSettingHash();

        ContractVerificationSubmitResponseRequest response = generateResponse(requestHash, privateKey);
        response.setCertificateID("81013556-5f56-4667-88c4-1c1f2d117549");
        response.setRequestID(request.getRequestID());
        response.setPublicKey(publicKeyString);
        try {
            JsonFileUtil.appendToJsonFile(filePath, response);
            System.out.println("Data appended to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
