package org.davex.chainmaker.controller.dto.request.certificateContract;

import lombok.Data;

@Data
public class ContractStoreCertificateRequest {
    private String certID;
    private String certificateHash; //证书内容
    private String createTime;
    private String updateTime;
}
