package org.davex.chainmaker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.chainmaker.pb.common.ResultOuterClass;
import org.davex.chainmaker.controller.dto.request.verificationContract.*;
import org.davex.chainmaker.controller.dto.response.ContractResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import org.davex.chainmaker.service.ContractVerificationService;

@Slf4j
@RestController
@RequestMapping("/api/contract/verification")
public class ControllerDoubleVerification {
    private final ContractVerificationService contractVerificationService;
    private final ObjectMapper jacksonObjectMapper;

    @Autowired
    public ControllerDoubleVerification(ContractVerificationService contractVerificationService, ObjectMapper jacksonObjectMapper) {
        this.contractVerificationService = contractVerificationService;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    @GetMapping("/query/all_blocks")
    public ContractResponse getAllTraceBlocks(@RequestBody ContractGetAllBlockRequest request) {
        try {
            log.debug("获取所有区块信息请求: {}", request);
            ResultOuterClass.TxResponse response = contractVerificationService.getAllTraceBlocks(request);
            String contractResult = response.getContractResult().getResult().toStringUtf8();
            log.debug("获取所有区块信息结果: {}", contractResult);

            Object jsonResult = jacksonObjectMapper.readValue(contractResult, Object.class);
            return ContractResponse.success(jsonResult);
        } catch (Exception e) {
            log.error("获取所有区块信息失败", e);
            return ContractResponse.error("获取所有区块信息失败");
        }
    }

    @GetMapping("/query/request")
    public ContractResponse getRequestById(@RequestBody ContractVerificationGetRequestByIDRequest request) {
        try {
            log.debug("获取验证请求: {}", request);
            ResultOuterClass.TxResponse response = contractVerificationService.getRequestById(request);
            String contractResult = response.getContractResult().getResult().toStringUtf8();
            log.debug("获取验证请求结果: {}", contractResult);

            Object jsonResult = jacksonObjectMapper.readValue(contractResult, Object.class);
            return ContractResponse.success(jsonResult);
        } catch (Exception e) {
            log.error("获取验证请求失败", e);
            return ContractResponse.error("获取验证请求失败");
        }
    }


    @GetMapping("/query/response")
    public ContractResponse getResponseById(@RequestBody ContractVerificationGetResponseByIDResponse contractVerificationGetResponseByID) {
        try {
            log.debug("获取验证响应: {}", contractVerificationGetResponseByID);
            ResultOuterClass.TxResponse response = contractVerificationService.getResponseById(contractVerificationGetResponseByID);
            String contractResult = response.getContractResult().getResult().toStringUtf8();
            log.debug("获取验证响应结果: {}", contractResult);

            Object jsonResult = jacksonObjectMapper.readValue(contractResult, Object.class);
            return ContractResponse.success(jsonResult);
        } catch (Exception e) {
            log.error("获取验证响应失败", e);
            return ContractResponse.error("获取验证响应失败");
        }
    }

    @GetMapping("/query/trace_block")
    public ContractResponse getTraceBlock(@RequestBody ContractVerificationGetTraceBlockRequest request) {
        try {
            log.debug("获取追溯区块信息: {}", request);
            ResultOuterClass.TxResponse response = contractVerificationService.getTraceBlock(request);
            String contractResult = response.getContractResult().getResult().toStringUtf8();
            log.debug("获取追溯区块信息结果: {}", contractResult);

            Object jsonResult = jacksonObjectMapper.readValue(contractResult, Object.class);
            return ContractResponse.success(jsonResult);
        } catch (Exception e) {
            log.error("获取追溯区块信息失败", e);
            return ContractResponse.error("获取追溯区块信息失败");
        }
    }

    @PostMapping("/invoke/submit_request")
    public ContractResponse submitVerificationRequest(@RequestBody ContractVerificationSubmitRequest request) {
        try {
            log.debug("提交验证请求: {}", request);
            ResultOuterClass.TxResponse response = contractVerificationService.submitVerificationRequest(request);
            String contractResult = response.getContractResult().getResult().toStringUtf8();
            System.out.println(contractResult);
            log.debug("提交验证请求结果: {}", contractResult);

            Object jsonResult = jacksonObjectMapper.readValue(contractResult, Object.class);
            return ContractResponse.success(jsonResult);
        } catch (Exception e) {
            log.error("提交验证请求失败", e);
            return ContractResponse.error("提交验证请求失败");
        }
    }

    @PostMapping("/invoke/submit_response")
    public ContractResponse submitVerificationResponse(@RequestBody ContractVerificationSubmitResponseRequest request) {
        try {
            log.debug("提交验证响应: {}", request);
            ResultOuterClass.TxResponse response = contractVerificationService.submitVerificationResponse(request);
            String contractResult = response.getContractResult().getResult().toStringUtf8();
            log.debug("提交验证响应结果: {}", contractResult);

            Object jsonResult = jacksonObjectMapper.readValue(contractResult, Object.class);
            return ContractResponse.success(jsonResult);
        } catch (Exception e) {
            log.error("提交验证响应失败", e);
            return ContractResponse.error("提交验证响应失败");
        }
    }

    @PostMapping("/invoke/verify_and_generate")
    public ContractResponse verifyAndGenerateTraceability(@RequestBody ContractVerificationVerifyAndGenerateRequest request) {
        try {
            log.debug("验证并生成追溯信息: {}", request);
            ResultOuterClass.TxResponse response = contractVerificationService.verifyAndGenerateTraceability(request);
            String contractResult = response.getContractResult().getResult().toStringUtf8();
            log.debug("验证并生成追溯信息结果: {}", contractResult);

            Object jsonResult = jacksonObjectMapper.readValue(contractResult, Object.class);
            return ContractResponse.success(jsonResult);
        } catch (Exception e) {
            log.error("验证并生成追溯信息失败", e);
            return ContractResponse.error("验证并生成追溯信息失败");
        }
    }

}
