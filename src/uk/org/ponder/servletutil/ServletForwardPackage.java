/*
 * Created on Jan 7, 2005
 */
package uk.org.ponder.servletutil;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import uk.org.ponder.streamutil.StreamCopier;
import uk.org.ponder.streamutil.StreamUtil;
import uk.org.ponder.stringutil.CharWrap;
import uk.org.ponder.stringutil.URLEncoder;
import uk.org.ponder.util.UniversalRuntimeException;

/**
 * Automates the process of forwarding a servlet request to a remote server.
 * After a call to <code>populate</code> the request parameters are available
 * in modifiable form in <code>parametermap</code> prior to dispatch via a
 * call to <code>dispatchTo</code>.
 * <p>Alternatively it may be used to conveniently forward a request
 * to a local servlet, with the parameters as modified in the parametermap.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *  
 */
public class ServletForwardPackage {
  public static final String CONTENT_TYPE = "Content-Type";
  public HttpServletRequest req;
  public HttpServletResponse res;
  public HashMap parametermap = new HashMap();
  public String targeturl;
  public String localurlbase;

  public void populate(HttpServletRequest req, HttpServletResponse res) {
    this.req = req;
    this.res = res;
    parametermap.clear();
    parametermap.putAll(req.getParameterMap());
  }

  public void forwardTo(String baseurl) {
    try {
    RequestDispatcher rd = req.getRequestDispatcher(baseurl);
    HttpServletRequestWrapper hsrw = new HttpServletRequestWrapper(req) {
      public Map getParameterMap() {
        return parametermap;
      }
      public String getParameter(String key) {
        String[] paramvals = getParameterValues(key);
        return (paramvals == null || paramvals.length == 0 ? null : paramvals[0]);
      }
      public String[] getParameterValues(String key) {
        return (String[]) parametermap.get(key);
      }
      public Enumeration getParameterNames() {
        return Collections.enumeration(parametermap.keySet());
      }
    };
    rd.forward(hsrw, res);
    }
    catch (Throwable t) {
      throw UniversalRuntimeException.accumulate(t,
          "Error forwarding servlet request to " + baseurl);
    }
  }
  
  public void dispatchTo(String baseurl) {
    CharWrap tobuild = new CharWrap();
    boolean first = true;
    for (Iterator it = parametermap.keySet().iterator(); it.hasNext();) {
      String key = (String) it.next();
      String value = (String) parametermap.get(key);
      tobuild.append(first ? ""
          : "&").append(URLEncoder.encode(key)).append('=').append(
          URLEncoder.encode(value));
      first = false;
    }
    String parameters = tobuild.toString();

    String method = req.getMethod();
    boolean ispost = method.equals("POST");
    String fullurl = baseurl + (ispost ? ""
        : "?" + parameters);
    try {
      URL URL = new URL(fullurl);
      URLConnection huc = URL.openConnection();
      if (ispost) {
        huc.setDoOutput(true);
        huc.setRequestProperty(CONTENT_TYPE,
            "application/x-www-form-urlencoded");
        OutputStream os = null;
        try {
          os = huc.getOutputStream();
          OutputStreamWriter osw = new OutputStreamWriter(os);
          osw.write(parameters);
        }
        finally {
          StreamUtil.closeOutputStream(os);
        }
        InputStream is = null;
        OutputStream clientout = null;
        try {
          is = huc.getInputStream();
          clientout = res.getOutputStream();
          StreamCopier.inputToOutput(is, clientout);
        }
        finally {
          StreamUtil.closeInputStream(is);
          StreamUtil.closeOutputStream(clientout);
        }
      }
    }
    catch (Throwable t) {
      throw UniversalRuntimeException.accumulate(t,
          "Error forwarding servlet request to " + baseurl);
    }
  }
}