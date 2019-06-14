package http.util;

public interface CharacterTransformUtil {
    public static final int CRLF = 4;
    public static final int DEFAULT = 0;
    public static final int NO_CLOSE = 16;
    public static final int NO_PADDING = 1;
    public static final int NO_WRAP = 2;
    public static final int URL_SAFE = 8;

    byte[] decode(String str, int flag) ;

    String encodeToString(byte[] data, int flag);

    byte[] decode(String str) ;

    String encodeToString(byte[] data);
}
