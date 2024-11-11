package org.davex.chainmaker.util;

import java.util.Base64;

public class SignatureEncoder {
    public static String encodeToBase64(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }
}
