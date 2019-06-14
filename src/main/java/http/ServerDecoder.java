package http;

import java.io.UnsupportedEncodingException;

//服务端校验接口
public interface ServerDecoder {
    //对时间戳进行校验，校验请求时间是否与服务端当前时间超过了5分钟
    boolean timesTampVerify(String timeStr);

    //获取AESKey
    //方案：从请求头SecurityKey字段中获取AESKeySecret字符串，并用RSA密钥对其进行解密，BASE64操作获取
    //AESKey
    String getAESKey(String securityKey) throws Exception;

    //获取bodyStr
    //方案：从报文中获取的是一段加密的httpBody，需要使用AESKey来进行解密。
    String getBodyStr(String encodeBody, String AESKey) throws Exception;

    //校验MD5签名
    //方案：计算出bodyStr的md5摘要；使用RSA密钥对signature字符串进行解密，得出rsaDecryptStr字符串；
    //比较两者是否一致，不一致则表示出错了
    boolean bodyVerify(String decodeBody, String signature) throws Exception;
}
