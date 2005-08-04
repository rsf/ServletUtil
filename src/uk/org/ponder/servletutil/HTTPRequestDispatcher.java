/*
 * Created on Dec 23, 2004
 */
package uk.org.ponder.servletutil;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import uk.org.ponder.saxalizer.XMLProvider;
import uk.org.ponder.streamutil.StreamCloseUtil;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.UniversalRuntimeException;

/** A utility class for clients of an InformationServlet. Accepts an object
 * to represent the request argument and a URL to dispatch it to, and does the
 * work of encoding it, performing the dispatch and decoding the response back
 * into an object.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *  
 */
public class HTTPRequestDispatcher {
  private String requestURL;
  private XMLProvider xmlprovider;

  public void setRequestURL(String URL) {
    this.requestURL = URL;
  }

  public void setXMLProvider(XMLProvider xmlprovider) {
    this.xmlprovider = xmlprovider;
  }

  public Object handleRequest(String requestURL, Object arg) {
    try {
      URL URL = new URL(requestURL);
      URLConnection huc = URL.openConnection();
      huc.setDoOutput(true);
      huc.setRequestProperty("Content-Type", "application/xml; charset=UTF-8");

      OutputStream os = null;
      try {
        os = huc.getOutputStream();
        xmlprovider.writeXML(arg, os);
        String debugstring = xmlprovider.toString(arg);
        Logger.log.info("HTTPRequestDispatcher sending data:\n" + debugstring);
      }
      finally {
        StreamCloseUtil.closeOutputStream(os);
      }
      
      InputStream is = null;
      try {
        is = huc.getInputStream();
        Object togo = xmlprovider.readXML(null, is);
        if (togo instanceof ErrorObject) {
          ErrorObject error = (ErrorObject) togo;
          Logger.log.warn("Remote exception intercepted in HTTPRequestDispatcher:\n" + error.message + 
              error.stacktrace.pack());
          throw new UniversalRuntimeException("Remote exception occurred during dispatching: " + error.message);
          
        }
        return togo;
      }
      finally {
        StreamCloseUtil.closeInputStream(is);
      }
    }
    catch (Throwable t) {
      throw UniversalRuntimeException.accumulate(t,
          "Error performing remote request to URL " + requestURL);
    }

  }
}