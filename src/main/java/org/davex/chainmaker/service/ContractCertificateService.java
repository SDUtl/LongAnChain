package org.davex.chainmaker.service;

import lombok.extern.slf4j.Slf4j;
import org.chainmaker.pb.common.ResultOuterClass;
import org.chainmaker.sdk.ChainClient;
import org.davex.chainmaker.controller.dto.request.certificateContract.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class ContractCertificateService {
    ChainClient chainClient;
    @Value("${chainmaker.certificate.contract.name}")
    private String contractName;

    @Value("${chainmaker.certificate.contract.invoke-method.store}")
    private String invokeMethodStore;

    @Value("${chainmaker.certificate.contract.query-method.get}")
    private String queryMethodGet;

    @Value("${chainmaker.certificate.contract.invoke-method.revoke}")
    private String invokeMethodRevoke;

    @Value("${chainmaker.certificate.contract.invoke-method.verify}")
    private String invokeMethodVerify;


    @Autowired
    public ContractCertificateService(ChainClient chainClient) {
        this.chainClient = chainClient;
    }

    public ResultOuterClass.TxResponse invokeCertificateContractStore(ContractStoreCertificateRequest request) {
        try {
            Map<String, byte[]> params = new HashMap<>();

            params.put("cert_id", request.getCertID().getBytes());
            params.put("certificate_hash", request.getCertificateHash().getBytes());
            params.put("create_time", request.getCreateTime().getBytes());
            params.put("update_time", request.getUpdateTime().getBytes());

            return chainClient.invokeContract(
                    contractName,
                    invokeMethodStore,
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

    public ResultOuterClass.TxResponse invokeCertificateContractGetInfo(ContractGetCertificateInfoRequest request) {
        try {
            Map<String, byte[]> params = new HashMap<>();

            params.put("cert_id", request.getCertID().getBytes());

            return chainClient.invokeContract(
                    contractName,
                    queryMethodGet,
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

    public ResultOuterClass.TxResponse invokeCertificateContractVerify(ContractVerifyCertificateRequest request) {
        try {
            Map<String, byte[]> params = new HashMap<>();

            params.put("cert_id", request.getCertID().getBytes());

            return chainClient.invokeContract(
                    contractName,
                    invokeMethodVerify,
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

    public ResultOuterClass.TxResponse invokeCertificateContractRevoke(ContractRevokeCertificateRequest request) {
        try {
            Map<String, byte[]> params = new HashMap<>();

            params.put("cert_id", request.getCertID().getBytes());
            params.put("update_time", request.getUpdateTime().getBytes());
            return chainClient.invokeContract(
                    contractName,
                    invokeMethodRevoke,
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
}