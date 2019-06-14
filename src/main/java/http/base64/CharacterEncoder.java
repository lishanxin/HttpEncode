package http.base64;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;

public abstract class CharacterEncoder {
  protected PrintStream pStream;
  
  protected abstract int bytesPerAtom();
  
  protected abstract int bytesPerLine();
  
  protected void encodeBufferPrefix(OutputStream aStream) throws IOException { this.pStream = new PrintStream(aStream); }
  
  protected void encodeBufferSuffix(OutputStream aStream) throws IOException {}
  
  protected void encodeLinePrefix(OutputStream aStream, int aLength) throws IOException {}
  
  protected void encodeLineSuffix(OutputStream aStream) throws IOException { this.pStream.println(); }
  
  protected abstract void encodeAtom(OutputStream paramOutputStream, byte[] paramArrayOfByte, int paramInt1, int paramInt2) throws IOException;
  
  protected int readFully(InputStream in, byte[] buffer) throws IOException {
    for (int i = 0; i < buffer.length; i++) {
      int q = in.read();
      if (q == -1)
        return i; 
      buffer[i] = (byte)q;
    } 
    return buffer.length;
  }
  
  public void encode(InputStream inStream, OutputStream outStream) throws IOException {
    byte[] tmpbuffer = new byte[bytesPerLine()];
    //添加开始标签
    encodeBufferPrefix(outStream);
    //进行每一行内容的读取
    while (true) {
      //读取一行的内容，并将信息存储于tmpbuffer中
      int numBytes = readFully(inStream, tmpbuffer);
      if (numBytes == 0)
        break;
      //添加行开始标签
      encodeLinePrefix(outStream, numBytes);
      //进行一行的转换
      for (int j = 0; j < numBytes; j += bytesPerAtom()) {
        //进行行内某一块的转换
        if (j + bytesPerAtom() <= numBytes) {
          //块的转换
          encodeAtom(outStream, tmpbuffer, j, bytesPerAtom());
        } else {
          encodeAtom(outStream, tmpbuffer, j, numBytes - j);
        } 
      } 
      if (numBytes < bytesPerLine())
        break;
      // 添加行结束标签
      encodeLineSuffix(outStream);
    }
    //添加结束标签
    encodeBufferSuffix(outStream);
  }
  
  public void encode(byte[] aBuffer, OutputStream aStream) throws IOException {
    ByteArrayInputStream inStream = new ByteArrayInputStream(aBuffer);
    encode(inStream, aStream);
  }

  //start Encode
  public String encode(byte[] aBuffer) {
    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    //put the data to ByteArrayInputStream
    ByteArrayInputStream inStream = new ByteArrayInputStream(aBuffer);
    String retVal = null;
    try {
      //start encode, put the result to outStream
      encode(inStream, outStream);
      retVal = outStream.toString("8859_1");
    } catch (Exception IOException) {
      throw new Error("CharacterEncoder.encode internal error");
    } 
    return retVal;
  }
  
  private byte[] getBytes(ByteBuffer bb) {
    byte[] buf = (byte[])null;
    if (bb.hasArray()) {
      byte[] tmp = bb.array();
      if (tmp.length == bb.capacity() && tmp.length == bb.remaining()) {
        buf = tmp;
        bb.position(bb.limit());
      } 
    } 
    if (buf == null) {
      buf = new byte[bb.remaining()];
      bb.get(buf);
    } 
    return buf;
  }
  
  public void encode(ByteBuffer aBuffer, OutputStream aStream) throws IOException {
    byte[] buf = getBytes(aBuffer);
    encode(buf, aStream);
  }
  
  public String encode(ByteBuffer aBuffer) {
    byte[] buf = getBytes(aBuffer);
    return encode(buf);
  }
  
  public void encodeBuffer(InputStream inStream, OutputStream outStream) throws IOException {
    int numBytes;
    byte[] tmpbuffer = new byte[bytesPerLine()];
    encodeBufferPrefix(outStream);
    do {
      numBytes = readFully(inStream, tmpbuffer);
      if (numBytes == 0)
        break; 
      encodeLinePrefix(outStream, numBytes);
      for (int j = 0; j < numBytes; j += bytesPerAtom()) {
        if (j + bytesPerAtom() <= numBytes) {
          encodeAtom(outStream, tmpbuffer, j, bytesPerAtom());
        } else {
          encodeAtom(outStream, tmpbuffer, j, numBytes - j);
        } 
      } 
      encodeLineSuffix(outStream);
    } while (numBytes >= bytesPerLine());
    encodeBufferSuffix(outStream);
  }
  
  public void encodeBuffer(byte[] aBuffer, OutputStream aStream) throws IOException {
    ByteArrayInputStream inStream = new ByteArrayInputStream(aBuffer);
    encodeBuffer(inStream, aStream);
  }
  
  public String encodeBuffer(byte[] aBuffer) {
    ByteArrayOutputStream outStream = new ByteArrayOutputStream();
    ByteArrayInputStream inStream = new ByteArrayInputStream(aBuffer);
    try {
      encodeBuffer(inStream, outStream);
    } catch (Exception IOException) {
      throw new Error("CharacterEncoder.encodeBuffer internal error");
    } 
    return outStream.toString();
  }
  
  public void encodeBuffer(ByteBuffer aBuffer, OutputStream aStream) throws IOException {
    byte[] buf = getBytes(aBuffer);
    encodeBuffer(buf, aStream);
  }
  
  public String encodeBuffer(ByteBuffer aBuffer) {
    byte[] buf = getBytes(aBuffer);
    return encodeBuffer(buf);
  }
}
