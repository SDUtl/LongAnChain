package org.davex.chainmaker.service;


import lombok.extern.slf4j.Slf4j;
import org.chainmaker.pb.common.ResultOuterClass;
import org.chainmaker.sdk.ChainClient;
import org.davex.chainmaker.controller.dto.request.verificationContract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ContractVerificationService {
    ChainClient chainClient;

    @Value("${chainmaker.verification.contract.name}")
    private String contractName;

    @Value("${chainmaker.verification.contract.invoke-method.submit_request}")
    private String invokeMethodSubmitRequest;

    @Value("${chainmaker.verification.contract.query-method.get_request_by_id}")
    private String queryMethodGetRequestByID;

    @Value("${chainmaker.verification.contract.invoke-method.submit_response}")
    private String invokeMethodSubmitResponse;

    @Value("${chainmaker.verification.contract.query-method.get_response_by_id}")
    private String queryMethodGetResponseByID;

    @Value("${chainmaker.verification.contract.query-method.get_all_trace_block}")
    private String queryMethodGetAllTraceBlock;

    @Value("${chainmaker.verification.contract.query-method.get_trace_block_by_id}")
    private String queryMethodGetTraceBlockByID;

    @Value("${chainmaker.verification.contract.invoke-method.verify_and_generate_traceability}")
    private String invokeMethodVerifyAndGenerateTraceability;

    @Autowired
    public ContractVerificationService(ChainClient chainClient) {
        this.chainClient = chainClient;
    }

    /**
     * 获取所有区块信息
     */
    public ResultOuterClass.TxResponse getAllTraceBlocks(ContractGetAllBlockRequest request) {
        try {
            Map<String, byte[]> params = new HashMap<>();
            params.put("trace_id", request.getTraceID().getBytes());

            return chainClient.queryContract(
                    contractName,
                    queryMethodGetAllTraceBlock,
                    null,
                    params,
                    1000,
                    chainClient.getClientUser()
            );
        } catch (Exception e) {
            log.error("获取所有区块信息失败", e);
            throw new RuntimeException("获取所有区块信息失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取验证请求
     */
    public ResultOuterClass.TxResponse getRequestById(ContractVerificationGetRequestByIDRequest request) {
        try {
            Map<String, byte[]> params = new HashMap<>();
            params.put("request_id", request.getRequestID().getBytes());

            return chainClient.queryContract(
                    contractName,
                    queryMethodGetRequestByID,
                    null,
                    params,
                    1000,
                    chainClient.getClientUser()
            );
        } catch (Exception e) {
            log.error("获取验证请求失败", e);
            throw new RuntimeException("获取验证请求失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID获取验证响应
     */
    public ResultOuterClass.TxResponse getResponseById(ContractVerificationGetResponseByIDResponse request) {
        try {
            Map<String, byte[]> params = new HashMap<>();
            params.put("response_id", request.getResponseID().getBytes());

            return chainClient.queryContract(
                   contractName,
                    queryMethodGetResponseByID,
                    null,
                    params,
                    1000,
                    chainClient.getClientUser()
            );
        } catch (Exception e) {
            log.error("获取验证响应失败", e);
            throw new RuntimeException("获取验证响应失败: " + e.getMessage());
        }
    }

    /**
     * 获取追溯区块信息
     */
    public ResultOuterClass.TxResponse getTraceBlock(ContractVerificationGetTraceBlockRequest request) {
        try {
            Map<String, byte[]> params = new HashMap<>();
            params.put("trace_block_id", request.getTraceBlockID().getBytes());

            return chainClient.queryContract(
                    contractName,
                    queryMethodGetTraceBlockByID,
                    null,
                    params,
                    1000,
                    chainClient.getClientUser()
            );
        } catch (Exception e) {
            log.error("获取追溯区块信息失败", e);
            throw new RuntimeException("获取追溯区块信息失败: " + e.getMessage());
        }
    }

    /**
     * 提交验证请求
     */
    public ResultOuterClass.TxResponse submitVerificationRequest(ContractVerificationSubmitRequest request) {
        try {
            Map<String, byte[]> params = new HashMap<>();
            params.put("request_id", request.getRequestID().getBytes());
            params.put("time_stamp", request.getTimeStamp().getBytes());
            params.put("file_description", request.getFileDescription().getBytes());
            params.put("sharing_setting_hash", request.getSharingSettingHash().getBytes());
            params.put("certificate_id", request.getCertificateID().getBytes());
            params.put("request_signature", request.getRequestSignature());

            return chainClient.invokeContract(
                    contractName,
                    invokeMethodSubmitRequest,
                    null,
                    params,
                    10000,
                    10000
            );
        } catch (Exception e) {
            log.error("提交验证请求失败", e);
            throw new RuntimeException("提交验证请求失败: " + e.getMessage());
        }
    }

    /**
     * 提交验证响应
     */
    public ResultOuterClass.TxResponse submitVerificationResponse(ContractVerificationSubmitResponseRequest request) {
        try {
            Map<String, byte[]> params = new HashMap<>();
            params.put("response_id", request.getResponseID().getBytes());
            params.put("request_id", request.getRequestID().getBytes());
            params.put("time_stamp", request.getTimeStamp().getBytes());
            params.put("certificate_id", request.getCertificateID().getBytes());
            params.put("response_signature", request.getResponseSignature());
            params.put("request_hash", request.getRequestHash());

            return chainClient.invokeContract(
                    contractName,
                    invokeMethodSubmitResponse,
                    null,
                    params,
                    10000,
                    10000
            );
        } catch (Exception e) {
            log.error("提交验证响应失败", e);
            throw new RuntimeException("提交验证响应失败: " + e.getMessage());
        }
    }

    /**
     * 验证并生成追溯信息
     */
    public ResultOuterClass.TxResponse verifyAndGenerateTraceability(ContractVerificationVerifyAndGenerateRequest request) {
        try {
            Map<String, byte[]> params = new HashMap<>();
            params.put("response_id", request.getResponseID().getBytes());
            params.put("time_stamp", request.getTimeStamp().getBytes());
            params.put("last_trace_id", request.getLastTraceID().getBytes());

            return chainClient.invokeContract(
                    contractName,
                    invokeMethodVerifyAndGenerateTraceability,
                    null,
                    params,
                    1000,
                    1000
            );
        } catch (Exception e) {
            log.error("验证并生成追溯信息失败", e);
            throw new RuntimeException("验证并生成追溯信息失败: " + e.getMessage());
        }
    }

}
