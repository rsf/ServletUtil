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
  private static ThreadLocal requestconsumerURLbase = new ThreadLocal();
  private static ThreadLocal rcresourceURLbase = new ThreadLocal();
  /** Set a thread-local state corresponding to the URL base required
   * for URLs holding dynamic content written to a remote consumer during 
   * this request cycle. This URL includes a trailing slash.
   */
  public static void setRequestConsumerURLBase(String urlbase) {
    requestconsumerURLbase.set(urlbase);
  }
  
  public static String getRequestConsumerURLBase() {
    return (String) requestconsumerURLbase.get();
  }

  /** Set a thread-local state corresponding to the URL base required
   * for statically served resource URLs written to a remote consumer 
   * during this request cycle. Note that this may not refer to the same
   * webapp or machine as the URL base above. We don't expect requests
   * for these URLs to pass through the dispatch of the remote consumer,
   * but be resolved directly.
   */
  public static void setRCResourceBase(String resourceurlbase) {
    rcresourceURLbase.set(resourceurlbase);
  }

  public static String getRCResourceURLBase() {
    return (String) rcresourceURLbase.get();
  }
  
  
  /** The "Base URL" is the full URL of this servlet, ignoring
   * any extra path due to the particular request.
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
  
  public static String getExtraPath(HttpServletRequest hsr) {
    String baseURL = getBaseURL(hsr);
    return hsr.getRequestURL().substring(baseURL.length());
  }

}
