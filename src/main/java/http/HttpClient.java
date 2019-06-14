package http;


import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class HttpClient {

    public static final String RSAPublicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCYLeyw4XhA14aGipzPDF+Iua3bG2hV4w1piTLZ\n" +
            "TblTwSipQHTLdoWv4uuEwl0s9vqHVkB/fs+MtrRQ9mx3P6OWxHw7STwYLI6C00M8TyVC0DmaatV8\n" +
            "uGvueSFZSBrxLujU3WcdRPzHiLu7Y7XmJeY8z0x5G9PPzSNgtgCBx37AqwIDAQAB";
    private static final String BASE_URL = "";
    ClientEnCoder enCoder = new ClientEnCoderSample();

    OkHttpClient client;

    public Call post(String url, String params){

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

        RequestBody body = RequestBody.create(MediaType.parse(""),httpBody);

        final Request request = new Request.Builder()
                .url(BASE_URL + url)
                .addHeader("timesTamp", System.currentTimeMillis() + "")
                .addHeader("signature", signature)
                .addHeader("AESKeySecret", AESKeySecret)
                .post(body)
                .build();
        return client.newCall(request);
    }



}
