package http.base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.nio.ByteBuffer;

public abstract class CharacterDecoder {
  protected abstract int bytesPerAtom();
  
  protected abstract int bytesPerLine();
  
  protected void decodeBufferPrefix(PushbackInputStream aStream, OutputStream bStream) throws IOException {}
  
  protected void decodeBufferSuffix(PushbackInputStream aStream, OutputStream bStream) throws IOException {}
  
  protected int decodeLinePrefix(PushbackInputStream aStream, OutputStream bStream) throws IOException { return bytesPerLine(); }
  
  protected void decodeLineSuffix(PushbackInputStream aStream, OutputStream bStream) throws IOException {}
  
  protected void decodeAtom(PushbackInputStream aStream, OutputStream bStream, int l) throws IOException { throw new CEStreamExhausted(); }
  
  protected int readFully(InputStream in, byte[] buffer, int offset, int len) throws IOException {
    for (int i = 0; i < len; i++) {
      int q = in.read();
      if (q == -1)
        return (i == 0) ? -1 : i; 
      buffer[i + offset] = (byte)q;
    } 
    return len;
  }
  
  public void decodeBuffer(InputStream aStream, OutputStream bStream) throws IOException {
    int totalBytes = 0;
    PushbackInputStream ps = new PushbackInputStream(aStream);
    decodeBufferPrefix(ps, bStream);
    try {
      while (true) {
        int length = decodeLinePrefix(ps, bStream);
        int i;
        for (i = 0; i + bytesPerAtom() < length; i += bytesPerAtom()) {
          decodeAtom(ps, bStream, bytesPerAtom());
          totalBytes += bytesPerAtom();
        } 
        if (i + bytesPerAtom() == length) {
          decodeAtom(ps, bStream, bytesPerAtom());
          totalBytes += bytesPerAtom();
        } else {
          decodeAtom(ps, bStream, length - i);
          totalBytes += length - i;
        } 
        decodeLineSuffix(ps, bStream);
      } 
    } catch (CEStreamExhausted cEStreamExhausted) {
      decodeBufferSuffix(ps, bStream);
      return;
    } 
  }
  
  public byte[] decodeBuffer(String inputString) throws IOException {
    inputString.getBytes("8859_1");
    byte[] inputBuffer = new byte[inputString.length()];
    inputString.getBytes(0, inputString.length(), inputBuffer, 0);
    ByteArrayInputStream inStream = new ByteArrayInputStream(inputBuffer);
    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    decodeBuffer(inStream, outStream);
    return outStream.toByteArray();
  }
  
  public byte[] decodeBuffer(InputStream in) throws IOException {
    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    decodeBuffer(in, outStream);
    return outStream.toByteArray();
  }
  
  public ByteBuffer decodeBufferToByteBuffer(String inputString) throws IOException { return ByteBuffer.wrap(decodeBuffer(inputString)); }
  
  public ByteBuffer decodeBufferToByteBuffer(InputStream in) throws IOException { return ByteBuffer.wrap(decodeBuffer(in)); }
}
