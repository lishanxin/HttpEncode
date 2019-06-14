package http.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

    //返回32位的md5签名信息
    public static String md5SignatureX32(String originStr){
        String jsonStrMD5 = null;
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(originStr.getBytes("UTF-8"));
            byte[] result = md5.digest();
            jsonStrMD5 = byteArray2HexStr(result);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return jsonStrMD5;
    }

    private static String byteArray2HexStr(byte[] bs) {
        StringBuilder md5StrBuff = new StringBuilder();
        for (byte b : bs) {
            if (Integer.toHexString(0xFF & b).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & b));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & b));
            }
        }
        return md5StrBuff.toString();
    }
}
