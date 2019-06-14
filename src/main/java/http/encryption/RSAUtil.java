package http.encryption;

import http.util.Base64Util;
import http.util.CharacterTransform;

import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

public class RSAUtil {

	public static final String PUBLIC_KEY = "RSAPublicKey";
	public static final String PRIVATE_KEY = "RSAPrivateKey";

	public static final String KEY_ALGORITHM = "RSA";
	/**
	 * 生成RSA的公钥和私钥
	 */
	public static Map<String, Object> initKey() throws Exception{
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(1024);  //512-65536 & 64的倍数
		KeyPair keyPair = keyPairGenerator.generateKeyPair();
		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		Map<String, Object> keyMap = new HashMap<String, Object>();
		keyMap.put(PUBLIC_KEY, publicKey);
		keyMap.put(PRIVATE_KEY, privateKey);
		
		return keyMap;
	}
	
	/**
	 * 获得公钥
	 */
	public static String getPublicKey(Map<String, Object> keyMap){
		RSAPublicKey publicKey = (RSAPublicKey) keyMap.get(PUBLIC_KEY);
		return CharacterTransform.encodeToString(publicKey.getEncoded());
	}
	
	/**
	 * 获得私钥
	 */
	public static String getPrivateKey(Map<String, Object> keyMap){
		RSAPrivateKey privateKey = (RSAPrivateKey) keyMap.get(PRIVATE_KEY);
		return CharacterTransform.encodeToString(privateKey.getEncoded());
	}

	/**
	 * 获取公钥对象
	 * @param keyStr 公钥Base64字符串
	 * @return Key 公钥对象
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	private static Key getPublicKey(String keyStr) throws NoSuchAlgorithmException, InvalidKeySpecException {
		// 对公钥解密
		byte[] keyBytes = CharacterTransform.decode(keyStr);

		// 取得公钥
		X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key publicKey = keyFactory.generatePublic(x509KeySpec);
		return publicKey;
	}

	/**
	 * 获取私钥对象
	 * @param keyStr 私钥Base64字符串
	 * @return Key 私钥对象
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 */
	private static Key getPrivateKey(String keyStr) throws NoSuchAlgorithmException, InvalidKeySpecException {
		// 对密钥解密
		byte[] keyBytes = CharacterTransform.decode(keyStr);

		// 取得私钥
		PKCS8EncodedKeySpec pkcs8KeySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance(KEY_ALGORITHM);
		Key privateKey = keyFactory.generatePrivate(pkcs8KeySpec);
		return privateKey;
	}
	
	/**
	 * 公钥加密
	 */
	public static byte[] encrypt(byte[] data, String publicKey) throws Exception{
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(publicKey));
		byte[] cipherBytes = cipher.doFinal(data);
		return cipherBytes;
	}

	/**
	 * 公匙
	 */
	
	/**
	 * 私钥解密
	 */
	public static byte[] decrypt(byte[] data, String privateKey) throws Exception{
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(privateKey));
		byte[] plainBytes = cipher.doFinal(data);
		return plainBytes;
	}
}
