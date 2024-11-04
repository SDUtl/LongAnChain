package org.davex.chainmaker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.chainmaker.pb.common.ResultOuterClass;
import org.davex.chainmaker.controller.dto.request.ContractInvokeRequest;
import org.davex.chainmaker.controller.dto.request.ContractQueryRequest;
import org.davex.chainmaker.controller.dto.response.ContractResponse;
import org.davex.chainmaker.service.ContractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contract/fact")
@Slf4j
public class ContractController {

    private final ContractService contractService;
    private final ObjectMapper jacksonObjectMapper;

    @Autowired
    public ContractController(ContractService contractService, ObjectMapper jacksonObjectMapper) {
        this.contractService = contractService;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    @PostMapping("/invoke/save_file")
    public ContractResponse invokeContract(@RequestBody ContractInvokeRequest request){
        try {
            ResultOuterClass.TxResponse response = contractService.invokeContract(request);
            String contractResult = response.getContractResult().getResult().toStringUtf8();

            //将字符串解析为JSON对象
            Object jsonResult = jacksonObjectMapper.readValue(contractResult, Object.class);
            return ContractResponse.success(jsonResult);
        }catch (Exception e){
            log.error("合约调用失败", e);
            return ContractResponse.error("合约调用失败");
        }
    }

    @GetMapping("/query/find_file")
    public ContractResponse queryContract(@RequestBody ContractQueryRequest request) {
        try {
            ResultOuterClass.TxResponse response = contractService.queryContract(request);

            String contractResult = response.getContractResult().getResult().toStringUtf8();

            //将字符串解析为JSON对象
            Object jsonResult = jacksonObjectMapper.readValue(contractResult, Object.class);

            return ContractResponse.success(jsonResult);
            //return ContractResponse.success(response);
        } catch (Exception e) {
            log.error("合约查询失败", e);
            return ContractResponse.error("合约查询失败");
        }
    }

}
