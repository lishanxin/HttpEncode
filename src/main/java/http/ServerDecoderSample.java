package http;

import http.encryption.AESUtil;
import http.encryption.RSAUtil;
import http.util.MD5Util;

public class ServerDecoderSample implements ServerDecoder {
    @Override
    public boolean timesTampVerify(String timeStr) {
        long serverTime = System.currentTimeMillis();
        long clientTime = Long.valueOf(timeStr);
        return serverTime > clientTime && (serverTime - clientTime)/1000/60 < 5;
    }

    @Override
    public String getAESKey(String securityKey) throws Exception {
        byte[] key = RSAUtil.decrypt(securityKey.getBytes(), ServerNetwork.RSAPrivateKey);
        return new String(key);
    }

    @Override
    public String getBodyStr(String encodeBody, String AESKey) throws Exception {
        byte[] data = AESUtil.decryptAES(encodeBody.getBytes("UTF-8"), AESKey);
        return new String(data, "UTF-8");
    }

    @Override
    public boolean bodyVerify(String decodeBody, String signature) throws Exception {
        byte[] md5Signature = RSAUtil.decrypt(signature.getBytes(), ServerNetwork.RSAPrivateKey);
        String md5Body = MD5Util.md5SignatureX32(decodeBody);
        return new String(md5Signature).equals(md5Body);
    }
}
