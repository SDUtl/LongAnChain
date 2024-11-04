package org.davex.chainmaker.controller.dto.request.verificationContract;

import lombok.Data;

@Data
public class ContractVerificationVerifyAndGenerateRequest {
    private String responseID;
    private String timeStamp;
    private String lastTraceID;
}
