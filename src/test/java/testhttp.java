import http.ClientEnCoder;
import http.ClientEnCoderSample;
import http.ServerDecoder;
import http.ServerDecoderSample;
import org.junit.Test;

public class testhttp {

    ClientEnCoder enCoder = new ClientEnCoderSample();
    ServerDecoder decoder = new ServerDecoderSample();

    @Test
    public void tedfdst() throws Exception {
        String params = "halsdfjlskfjaslkfjaskfjd;lasfkj";
        String signature = null;
        String httpBody = null;
        String AESKeySecret = null;
        try {
            String AESKey = enCoder.getAESKey();

            signature = enCoder.getBodySignature(params);
            httpBody = enCoder.getEncodeBody(params, AESKey);
            AESKeySecret = enCoder.getEncodeAESKey(AESKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String AESKey = decoder.getAESKey(AESKeySecret);
        String decodeBody = decoder.getBodyStr(httpBody, AESKey);
        boolean signatureCorrect = decoder.bodyVerify(decodeBody, signature);

        System.out.println("decodeBody is: " + decodeBody);
        System.out.println("signatureCorrect is: " + signatureCorrect);
    }
}
