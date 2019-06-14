

import http.*;
import http.algorithm.rsa;
import http.encryption.BytesToHex;
import http.encryption.RSAUtil;
import http.util.Base64Util;
import http.util.CharacterTransform;
import org.junit.Test;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;



public class testRSA {

	//待加密原文
	public static final String DATA = "hi, welcome to my git area!添加烦死了快递费尽量少开飞机";

	static ClientEnCoder enCoder = new ClientEnCoderSample();
	static ServerDecoder decoder = new ServerDecoderSample();
	
	public static void main(String[] args) throws Exception {
		Map<String, Object> keyMap = RSAUtil.initKey();


		String rsaPublicKey = RSAUtil.getPublicKey(keyMap);
		String rsaPrivateKey = RSAUtil.getPrivateKey(keyMap);
		System.out.println("RSA PublicKey: " + rsaPublicKey);
		System.out.println("RSA PrivateKey: " + rsaPrivateKey);

		String AESKey = enCoder.getAESKey();

		byte[] AESKeySecretByte = RSAUtil.encrypt(AESKey.getBytes(), rsaPublicKey);
//		String AESKeySecretStr = CharacterTransform.encodeToString(AESKeySecretByte);
		String AESKeySecretStr = new String(AESKeySecretByte, "8859_1");

		String AESKeyDecode = new String(RSAUtil.decrypt(AESKeySecretStr.getBytes("8859_1"), rsaPrivateKey), "8859_1") ;
		System.out.println("AES 1: " + AESKey);
		System.out.println("AES 2: " + AESKeyDecode);

		byte[] rsaResult = RSAUtil.encrypt(DATA.getBytes(), rsaPublicKey);
		System.out.println(DATA + "====>>>> RSA 加密>>>>====" + BytesToHex.fromBytesToHex(rsaResult));
		
		byte[] plainResult = RSAUtil.decrypt(rsaResult, rsaPrivateKey);
		System.out.println(new String(DATA.getBytes(), "UTF-8") + new String("====>>>> RSA 解密>>>>====".getBytes("GBK"), "UTF-8") + new String(plainResult, "UTF-8"));
	}

	@Test
	public void rsaTest() throws Exception {
		Map<String, Object> keyMap = rsa.initKey();

		String rsaPublicKey = rsa.getPublicKey(keyMap);
		String rsaPrivateKey = rsa.getPrivateKey(keyMap);
		System.out.println("RSA PublicKey: " + rsaPublicKey);
		System.out.println("RSA PrivateKey: " + rsaPrivateKey);

		byte[] rsaResult = rsa.encryptByPublicKey(DATA.getBytes(), rsaPublicKey);
		System.out.println(DATA + "====>>>> RSA 加密>>>>====" + BytesToHex.fromBytesToHex(rsaResult));

		byte[] plainResult = rsa.decryptByPrivateKey(rsaResult, rsaPrivateKey);
		System.out.println(new String(DATA.getBytes(), "UTF-8") + new String("====>>>> RSA 解密>>>>====".getBytes("GBK"), "UTF-8") + new String(plainResult, "UTF-8"));
	}
}
