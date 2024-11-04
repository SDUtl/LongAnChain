package org.davex.chainmaker.controller.dto.request.certificateContract;

import lombok.Data;

@Data
public class ContractRevokeCertificateRequest {
    private String certID;
    private String updateTime;
}
