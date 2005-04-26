/*
 * Created on Jan 7, 2005
 */
package uk.org.ponder.servletutil;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import uk.org.ponder.streamutil.StreamCopier;
import uk.org.ponder.streamutil.StreamCopyUtil;
import uk.org.ponder.streamutil.StreamUtil;
import uk.org.ponder.stringutil.CharWrap;
import uk.org.ponder.stringutil.URLEncoder;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.UniversalRuntimeException;

/**
 * Automates the process of forwarding a servlet request to a remote server.
 * After a call to <code>populate</code> the request parameters are available
 * in modifiable form in <code>parametermap</code> prior to dispatch via a
 * call to <code>dispatchTo</code>.
 * <p>
 * Alternatively it may be used to conveniently forward a request to a local
 * servlet, with the parameters as modified in the parametermap.
 * <p>
 * ServletForwardPackages are highly non-threadsafe and should be destroyed
 * after use.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *  
 */
public class ServletForwardPackage {
  public static final String CONTENT_TYPE = "Content-Type";
  public HttpServletRequest req;
  public HttpServletResponse res;
  public HashMap parametermap = new HashMap();
  public String targeturl;
  //public String localurlbase;
  public StreamCopier streamcopier;
  public String charencoding = "UTF-8";
   
  // assume no multiple-valued parameters from US.
  public void addParameter(String key, String value) {
    parametermap.put(key, new String[] {value});
  }
  
  private static ThreadLocal copybuffer = new ThreadLocal();
  private static byte[] getBuffer() {
    byte[] togo = (byte[])copybuffer.get();
    if (togo == null) {
      togo = new byte[8192];
      copybuffer.set(togo);
    }
    return togo;
  }
  
  private static StreamCopier defaultcopier = new StreamCopier() {
    public void copyStream(InputStream in, OutputStream out) {
      StreamCopyUtil.inputToOutput(in, out, getBuffer());
    }    
  };
  private boolean unwrapredirect;

  /** Initialise this ForwardPackage with details taken from the specified
   * request and response objects.
   */
  public void populate(HttpServletRequest req, HttpServletResponse res) {
    this.req = req;
    this.res = res;
    this.streamcopier = defaultcopier;
    parametermap.clear();
    parametermap.putAll(req.getParameterMap());
  }

  public void forwardTo(String baseurl) {
    Logger.log.info("**ServletForwardPackage beginning LOCAL forward to baseurl "
                + baseurl);

    try {
      RequestDispatcher rd = req.getRequestDispatcher(baseurl);
      HttpServletRequestWrapper hsrw = new HttpServletRequestWrapper(req) {
        public Map getParameterMap() {
          return parametermap;
        }

        public String getParameter(String key) {
          String[] paramvals = getParameterValues(key);
          return (paramvals == null || paramvals.length == 0 ? null
              : paramvals[0]);
        }

        public String[] getParameterValues(String key) {
          return (String[]) parametermap.get(key);
        }

        public Enumeration getParameterNames() {
          return Collections.enumeration(parametermap.keySet());
        }
      };
      if (streamcopier == defaultcopier) {
        rd.forward(hsrw, res);
      }
      else throw new UniversalRuntimeException("Filtered local forward not yet implemented");
    }
    catch (Throwable t) {
      throw UniversalRuntimeException.accumulate(t,
          "Error forwarding servlet request to " + baseurl);
    }
  }

  public void setUnwrapRedirect(boolean unwrapredirect) {
    this.unwrapredirect = unwrapredirect;
  }
  
  /** Perform a dispatch of this request to a remote servlet using raw
   * HTTP. Return the target URL if the remote servlet issues a redirect, 
   * otherwise null.
   */
  public String dispatchTo(String baseurl) {
    String location = null;
   Logger.log.info(
        "**ServletForwardPackage beginning REMOTE dispatch to baseurl "
            + baseurl);
    CharWrap tobuild = new CharWrap();
    boolean first = true;
    for (Iterator it = parametermap.keySet().iterator(); it.hasNext();) {
      String key = (String) it.next();
      String[] value = (String[]) parametermap.get(key);
      for (int i = 0; i < value.length; ++i) {
        tobuild.append(first ? ""
            : "&").append(URLEncoder.encode(key)).append('=').append(
            URLEncoder.encode(value[i]));
      }
      first = false;
    }
    String parameters = tobuild.toString();
   Logger.log.info(
        "**ServletForwardPackage composed parameter string " + parameters);

    String method = req.getMethod();
    boolean ispost = method.equals("POST");
    String fullurl = baseurl + (ispost ? ""
        : "?" + parameters);
    try {
      Logger.log.info("URL length: " + fullurl.length() + " characters");
      URL URL = new URL(fullurl);
      HttpURLConnection huc = (HttpURLConnection) URL.openConnection();
      huc.setInstanceFollowRedirects(!unwrapredirect);
      if (ispost) {
        huc.setRequestMethod("POST");
        huc.setDoOutput(true);
        huc.setRequestProperty(CONTENT_TYPE,
            "application/x-www-form-urlencoded; charset=UTF-8");
        OutputStreamWriter osw = null;
        try {
          OutputStream os = huc.getOutputStream();
          osw = new OutputStreamWriter(os, charencoding);
          osw.write(parameters);
        }
        finally {
          StreamUtil.closeWriter(osw);
        }
      }
      InputStream is = null;
      OutputStream clientout = null;
      try {
        int response = huc.getResponseCode();
     
        if (unwrapredirect && response >= 300 && response < 400) {
          Logger.log.info("Received redirect response with message: " +  
              huc.getResponseMessage());
          location = huc.getHeaderField("Location");
          Logger.log.info("Issuing client redirect to location " + location);
          res.sendRedirect(location);
        }
        else {
          String contenttype = huc.getContentType();
          Logger.log.info("Forwarding for received content type " + contenttype);
          res.setContentType(contenttype);
          clientout = res.getOutputStream();
          is = huc.getInputStream();
          //is = StreamCopyUtil.bottleToDisk(is, "e:\\courseworkpage.txt");
          streamcopier.copyStream(is, clientout);
        }
      }
      finally {
        StreamUtil.closeInputStream(is);
        StreamUtil.closeOutputStream(clientout);
      }
    }
    catch (Throwable t) {
      throw UniversalRuntimeException.accumulate(t,
          "Error dispatching servlet request to " + baseurl);
    }
    return location;
  }
}