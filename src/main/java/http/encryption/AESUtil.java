package http.encryption;

import http.util.Base64Util;
import http.util.CharacterTransform;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class AESUtil {
    /**
     * 生成密钥
     * @throws Exception
     */
    public static String initKey() throws Exception{
        //密钥生成器
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        //初始化密钥生成器
        keyGen.init(128);  //默认128，获得无政策权限后可用192或256
        //生成密钥
        SecretKey secretKey = keyGen.generateKey();
        return CharacterTransform.encodeToString(secretKey.getEncoded());
    }

    /**
     * 加密
     * @throws Exception
     */
    public static byte[] encryptAES(byte[] data, String base64Key) throws Exception{
        byte[] key = CharacterTransform.decode(base64Key);
        //恢复密钥
        SecretKey secretKey = new SecretKeySpec(key, "AES");
        //Cipher完成加密
        Cipher cipher = Cipher.getInstance("AES");
        //根据密钥对cipher进行初始化
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        //加密
        return cipher.doFinal(data);
    }
    /**
     * 解密
     */
    public static byte[] decryptAES(byte[] data, String base64Key) throws Exception{
        byte[] key = CharacterTransform.decode(base64Key);
        //恢复密钥生成器
        assert key != null;
        SecretKey secretKey = new SecretKeySpec(key, "AES");
        //Cipher完成解密
        Cipher cipher = Cipher.getInstance("AES");
        //根据密钥对cipher进行初始化
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return cipher.doFinal(data);
    }
}
