package org.davex.chainmaker.controller.dto.request.verificationContract;

import lombok.Data;

@Data
public class ContractVerificationSubmitRequest {
    private String requestID;
    private String timeStamp;
    private String fileDescription;
    private String sharingSettingHash;
    private String certificateID;
    private byte[] requestSignature;
}
