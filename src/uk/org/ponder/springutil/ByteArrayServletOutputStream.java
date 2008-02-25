/*
 * Created on 24 Nov 2006
 */
package uk.org.ponder.springutil;

import java.io.ByteArrayOutputStream;

import javax.servlet.ServletOutputStream;

public class ByteArrayServletOutputStream extends ServletOutputStream {
  private ByteArrayOutputStream baos = new ByteArrayOutputStream();
  public void write(int b)  {
    baos.write(b);
  }

  public byte[] toByteArray() {
    return baos.toByteArray();
  }

}
