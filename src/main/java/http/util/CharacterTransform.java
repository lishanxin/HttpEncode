package http.util;

public class CharacterTransform {

    static CharacterTransformUtil util = Base64Util.getInstance();

    public static byte[] decode(String str) {
       return util.decode(str);
    }

    public static String encodeToString(byte[] data){
        return util.encodeToString(data);
    }
}
