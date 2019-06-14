package http.util;


import http.base64.BASE64Decoder;
import http.base64.BASE64Encoder;
import http.base64.CharacterDecoder;
import http.base64.CharacterEncoder;

import java.io.IOException;

public class Base64Util implements CharacterTransformUtil {

    private static Base64Util instance;

    private Base64Util(){

    }

    public static Base64Util getInstance(){
        if (instance == null){
            synchronized (Factory.class){
                if (instance == null)
                    instance = new Base64Util();
            }
        }
        return instance;
    }

    @Override
    public byte[] decode(String str, int flag) {
        try {
            return Factory.getInstance().decoder().decodeBuffer(str);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public String encodeToString(byte[] data, int flag){
        return Factory.getInstance().encoder().encode(data);
    }

    @Override
    public byte[] decode(String str) {
        return decode(str, CharacterTransformUtil.DEFAULT);
    }

    @Override
    public String encodeToString(byte[] data) {
        return encodeToString(data, CharacterTransformUtil.DEFAULT);
    }


    public static class Factory{

        private CharacterDecoder decoder = new BASE64Decoder();
        private CharacterEncoder encoder = new BASE64Encoder();

        private static Factory instance;

        public static Factory getInstance(){
            if (instance == null){
                synchronized (Factory.class){
                    if (instance == null)
                        instance = new Factory();
                }
            }
            return instance;
        }

        public CharacterDecoder decoder(){
            return decoder;
        }

        public CharacterEncoder encoder(){
            return encoder;
        }
    }
}
