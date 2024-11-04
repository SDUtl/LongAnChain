package org.davex.chainmaker.service;

import org.davex.chainmaker.config.InitClient;
import org.davex.chainmaker.controller.dto.request.ContractInvokeRequest;
import org.davex.chainmaker.controller.dto.request.ContractQueryRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.chainmaker.pb.common.ResultOuterClass;
import org.chainmaker.sdk.ChainClient;
import org.chainmaker.sdk.ChainClientException;
import org.chainmaker.sdk.crypto.ChainMakerCryptoSuiteException;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import static org.davex.chainmaker.config.InitClient.initChainClientForPk;
@Slf4j
@Service
public class ContractService {
    ChainClient chainClient;

    @Value("${chainmaker.contract.name}")
    private String contractName;

    @Value("${chainmaker.contract.invoke-method}")
    private String invokeMethod;

    @Value("${chainmaker.contract.query-method}")
    private String queryMethod;

    @Autowired
    public ContractService(ChainClient chainClient) {
        this.chainClient = chainClient;
    }

    public ResultOuterClass.TxResponse invokeContract(ContractInvokeRequest request) {
        try {
            Map<String, byte[]> params = new HashMap<>();

            params.put("file_hash", request.getFileHash().getBytes());
            params.put("file_name", request.getFileName().getBytes());
            params.put("time", request.getTime().getBytes());

            return chainClient.invokeContract(
                    contractName,
                    invokeMethod,
                    null,
                    params,
                    10000,
                    10000
            );
        } catch (Exception e) {
            log.error("调用合约失败", e);
            throw new RuntimeException("调用合约失败: " + e.getMessage());
        }
    }

    public ResultOuterClass.TxResponse queryContract(ContractQueryRequest request) {
        try {
            Map<String, byte[]> params = new HashMap<>();
            params.put("file_hash", request.getFileHash().getBytes());

            return chainClient.queryContract(
                    contractName,
                    queryMethod,
                    null,
                    params,
                    10000
            );
        } catch (Exception e) {
            log.error("查询合约失败", e);
            throw new RuntimeException("查询合约失败: " + e.getMessage());
        }
    }
}
