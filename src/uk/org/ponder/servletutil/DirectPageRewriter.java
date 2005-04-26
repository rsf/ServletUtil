/*
 * Created on Feb 2, 2005
 */
package uk.org.ponder.servletutil;

import java.io.InputStream;
import java.io.OutputStream;

import uk.org.ponder.streamutil.StreamCopier;
import uk.org.ponder.streamutil.StreamCopyUtil;
import uk.org.ponder.util.UniversalRuntimeException;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *  
 */
public class DirectPageRewriter implements StreamCopier {
  public static final int COPY_BUF_SIZ = 4092;
 
  private byte[] header1bytes;
  private byte[] header2bytes;
  private byte[] footerbytes;

  static ThreadLocal bytebuffer = new ThreadLocal() {
    public Object initialValue() {
      return new byte[COPY_BUF_SIZ];
    }
  };
  // Header2 currently ignored.
  public void setHeaderFooter(String header1, String header2,
      String footer, String encoding) {
    try {
      header1bytes = header1.getBytes(encoding);
      header2bytes = header2.getBytes(encoding);
      footerbytes = footer.getBytes(encoding);
    }
    catch (Throwable t) {
      throw UniversalRuntimeException.accumulate(t,
          "Error converting headers to encoding " + encoding);
    }
  
  }

  public void copyStream(InputStream in, OutputStream out) {
    byte[] copybuf = (byte[]) bytebuffer.get();
    try {
      out.write(header1bytes);
      StreamCopyUtil.inputToOutput(in, out, true, false, copybuf);
      out.write(footerbytes);
    }
    catch (Throwable t) {
      throw UniversalRuntimeException.accumulate(t, "Error writing headers");
    }
  }

}