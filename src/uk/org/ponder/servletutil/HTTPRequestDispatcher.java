/*
 * Created on Dec 23, 2004
 */
package uk.org.ponder.servletutil;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import uk.org.ponder.saxalizer.XMLProvider;
import uk.org.ponder.streamutil.StreamUtil;
import uk.org.ponder.util.UniversalRuntimeException;

/**
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
      huc.setRequestProperty("Content-Type", "application/xml");

      OutputStream os = null;
      try {
        os = huc.getOutputStream();
        xmlprovider.writeXML(arg, os);
      }
      finally {
        StreamUtil.closeOutputStream(os);
      }
      
      InputStream is = null;
      try {
        is = huc.getInputStream();
        Object togo = xmlprovider.readXML(null, is);
        return togo;
      }
      finally {
        StreamUtil.closeInputStream(is);
      }
    }
    catch (Throwable t) {
      throw UniversalRuntimeException.accumulate(t,
          "Error performing remote request to URL " + requestURL);
    }

  }
}