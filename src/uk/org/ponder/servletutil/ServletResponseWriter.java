/*
 * Created on May 9, 2006
 */
package uk.org.ponder.servletutil;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletResponse;

import uk.org.ponder.streamutil.StreamCloseUtil;
import uk.org.ponder.util.UniversalRuntimeException;

public class ServletResponseWriter {
  private ByteArrayOutputStream baos;
  private HttpServletResponse response;
  
  private class ServletBAOS extends ByteArrayOutputStream {
    public void close() {
      byte[] bytes = baos.toByteArray();
    
      OutputStream os = null;
      try {
        os = response.getOutputStream();
        response.setContentLength(bytes.length);
        response.getOutputStream();
        os.write(bytes);
      }
      catch (Exception e) {
        throw UniversalRuntimeException.accumulate(e, "Error writing response");
      }
      finally {
        StreamCloseUtil.closeOutputStream(os);
      }
    }
  }
  
  public ServletResponseWriter(HttpServletResponse response) {
    this.response = response;
    baos = new ServletBAOS();
  }
  
  public OutputStream getOutputStream() {
    return baos;
  }
}
