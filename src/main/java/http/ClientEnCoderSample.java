package http;


import http.encryption.AESUtil;
import http.encryption.RSAUtil;
import http.util.MD5Util;

public class ClientEnCoderSample implements ClientEnCoder {
    @Override
    public String getBodySignature(String originStr) throws Exception {
        String originMD5 = MD5Util.md5SignatureX32(originStr);
        byte[] signature = RSAUtil.encrypt(originMD5.getBytes(), HttpClient.RSAPublicKey);
        return new String(signature);
    }

    @Override
    public String getEncodeBody(String originStr, String AESKey) throws Exception {
        byte[] data = AESUtil.encryptAES(originStr.getBytes("UTF-8"), AESKey);
        return new String(data, "UTF-8");
    }

    @Override
    public String getEncodeAESKey(String AESKey) throws Exception {
        byte[] key = RSAUtil.encrypt(AESKey.getBytes(), HttpClient.RSAPublicKey);
        return new String(key);
    }

    @Override
    public String getAESKey() throws Exception {
        return AESUtil.initKey();
    }



}
