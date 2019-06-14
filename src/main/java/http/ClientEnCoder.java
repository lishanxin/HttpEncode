package http;

public interface ClientEnCoder {

    //generic Signature zh:传输对象生成签名
    // Post请求时，对传输的BODY进行签名获取，以便后端进行校验传输对象是否被修改
    //方案：获取jsonStr的md5摘要；使用RSA公钥对md5摘要进行加密，生成签名。
    String getBodySignature(String originStr) throws Exception;

    //加密请求报文，对Body字符串进行加密，再传输。
    //方案：动态生成一个AESKey，并用其对jsonStr进行加密；
    String getEncodeBody(String originStr, String AESKey) throws Exception;

    //加密AESKey
    //方案：将AESKey使用Base64转换为AESKeyStr;使用RSA公钥对AESKeyStr进行加密，生成AESKeySecert
    String getEncodeAESKey(String AESKey) throws Exception;

    //生成AESKey
    String getAESKey() throws Exception;
}
