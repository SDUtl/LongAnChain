package org.davex.chainmaker.controller;

import org.davex.chainmaker.service.ContractCertificateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.chainmaker.pb.common.ResultOuterClass;
import org.davex.chainmaker.controller.dto.request.certificateContract.*;
import org.davex.chainmaker.controller.dto.response.ContractResponse;
import org.davex.chainmaker.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contract/certificate")
@Slf4j
public class ControllerCertificate {
    private final ContractCertificateService contractCertificateService;
    private final ObjectMapper jacksonObjectMapper;

    @Autowired
    public ControllerCertificate(ContractCertificateService contractCertificateService, ObjectMapper jacksonObjectMapper) {
        this.contractCertificateService = contractCertificateService;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    @PostMapping("/invoke/store_certificate")
    public ContractResponse invokeContractCertificateStore(@RequestBody ContractStoreCertificateRequest request){
        try {
            System.out.println(request);
            ResultOuterClass.TxResponse response = contractCertificateService.invokeCertificateContractStore(request);
            System.out.println(request);
            String contractResult = response.getContractResult().getResult().toStringUtf8();

            System.out.println(contractResult);
            //将字符串解析为JSON对象
            Object jsonResult = jacksonObjectMapper.readValue(contractResult, Object.class);
            return ContractResponse.success(jsonResult);
        }catch (Exception e){
            log.error("合约调用失败", e);
            return ContractResponse.error("合约调用失败");
        }
    }

    @PostMapping("/invoke/revoke_certificate")
    public ContractResponse invokeContractCertificateRevoke(@RequestBody ContractRevokeCertificateRequest request){
        try {
            ResultOuterClass.TxResponse response = contractCertificateService.invokeCertificateContractRevoke(request);
            String contractResult = response.getContractResult().getResult().toStringUtf8();

            //将字符串解析为JSON对象
            Object jsonResult = jacksonObjectMapper.readValue(contractResult, Object.class);
            return ContractResponse.success(jsonResult);
        }catch (Exception e){
            log.error("合约调用失败", e);
            return ContractResponse.error("合约调用失败");
        }
    }

    @PostMapping("/invoke/verify_certificate")
    public ContractResponse invokeContractCertificateVerify(@RequestBody ContractVerifyCertificateRequest request){
        try {
            ResultOuterClass.TxResponse response = contractCertificateService.invokeCertificateContractVerify(request);
            String contractResult = response.getContractResult().getResult().toStringUtf8();

            //将字符串解析为JSON对象
            Object jsonResult = jacksonObjectMapper.readValue(contractResult, Object.class);
            return ContractResponse.success(jsonResult);
        }catch (Exception e){
            log.error("合约调用失败", e);
            return ContractResponse.error("合约调用失败");
        }
    }

    @GetMapping("/query/get_certificate")
    public ContractResponse invokeContractCertificateGet(@RequestBody ContractGetCertificateInfoRequest request){
        try {
            ResultOuterClass.TxResponse response = contractCertificateService.invokeCertificateContractGetInfo(request);
            String contractResult = response.getContractResult().getResult().toStringUtf8();

            //将字符串解析为JSON对象
            Object jsonResult = jacksonObjectMapper.readValue(contractResult, Object.class);
            return ContractResponse.success(jsonResult);
        }catch (Exception e){
            log.error("合约调用失败", e);
            return ContractResponse.error("合约调用失败");
        }
    }


}
