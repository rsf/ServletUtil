/*
 * Created on Dec 9, 2004
 */
package uk.org.ponder.servletutil;

import javax.servlet.http.HttpServletRequest;

import uk.org.ponder.util.UniversalRuntimeException;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class ServletUtil {
  /** The "Base URL" is the full URL of this servlet, ignoring
   * any extra path.
   */
  public static String getBaseURL(HttpServletRequest hsr) {
    String requestURL = hsr.getRequestURL().toString();
    String requestpath = hsr.getServletPath();
    int embedpoint = requestURL.indexOf(requestpath);
    if (embedpoint == -1) {
      throw new UniversalRuntimeException("Cannot locate request path of "
          + requestpath + " within request URL of " + requestURL);
    }
    String baseURL = requestURL.substring(0, embedpoint + requestpath.length()
        + 1);
    return baseURL; 
  }
}
