package org.davex.chainmaker.util;

import java.util.Base64;

public class SignatureDecoder {
    public static byte[] decodeBase64String(String base64String) {
        return Base64.getDecoder().decode(base64String);
    }
}
