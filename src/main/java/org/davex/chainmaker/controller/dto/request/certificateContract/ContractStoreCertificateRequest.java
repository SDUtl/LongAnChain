package org.davex.chainmaker.controller.dto.request.certificateContract;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.davex.chainmaker.util.JsonFileUtil;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.UUID;

@Data
public class ContractStoreCertificateRequest {
    private String certID;
    private String certificateHash; //证书内容
    private String createTime;
    private String updateTime;

    // 生成示例实例
    public static ContractStoreCertificateRequest generateExample() {
        ContractStoreCertificateRequest request = new ContractStoreCertificateRequest();

        // 生成 certID (UUID)
        request.setCertID(UUID.randomUUID().toString());

        // 模拟 certificateHash (Base64 编码的随机数据哈希)
        request.setCertificateHash(generateMockCertificateHash());

        // 设置 createTime 和 updateTime 为当前时间
        request.setCreateTime(String.valueOf(System.currentTimeMillis()));
        request.setUpdateTime(String.valueOf(System.currentTimeMillis()));

        return request;
    }

    // 生成模拟的证书哈希
    private static String generateMockCertificateHash() {
        return Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes());
    }


    public static void main(String[] args) {
        // 生成并输出示例数据
        ContractStoreCertificateRequest example = generateExample();
        System.out.println("Generated ContractStoreCertificateRequest:");
        System.out.println("Cert ID: " + example.getCertID());
        System.out.println("Certificate Hash: " + example.getCertificateHash());
        System.out.println("Create Time: " + example.getCreateTime());
        System.out.println("Update Time: " + example.getUpdateTime());

        String filePath = "output.json";
        try {
            JsonFileUtil.appendToJsonFile(filePath, example);
            System.out.println("Data appended to " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
