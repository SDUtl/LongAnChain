package org.davex.chainmaker.controller.dto.request.verificationContract;

import lombok.Data;

@Data
public class ContractVerificationSubmitResponseRequest {
    private String responseID;
    private String requestID;
    private String timeStamp;
    private String certificateID;
    private byte[] responseSignature;
    private byte[] requestHash;
 }
