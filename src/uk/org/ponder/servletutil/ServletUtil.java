/*
 * Created on Dec 9, 2004
 */
package uk.org.ponder.servletutil;

import javax.servlet.http.HttpServletRequest;

import uk.org.ponder.util.UniversalRuntimeException;
import uk.org.ponder.webapputil.ConsumerRequestInfo;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class ServletUtil {
  
  /** The "Base URL" is the full URL of this servlet, ignoring
   * any extra path due to the particular request.
   */
  // TODO: It might well be more reliable to do this by simply knocking
  // off "PathInfo" from RequestURL. Note that RequestURL can be the
  // ORIGINAL url (before a dispatch) and hence not agree with a URL that
  // could be used to invoke the current request.
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
  
  public static String getExtraPath(HttpServletRequest hsr) {
    String baseURL = getBaseURL(hsr);
    return hsr.getRequestURL().substring(baseURL.length());
  }

}
