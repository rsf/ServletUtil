/*
 * Created on Dec 9, 2004
 */
package uk.org.ponder.servletutil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import uk.org.ponder.beanutil.BeanGetter;
import uk.org.ponder.util.UniversalRuntimeException;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class ServletUtil {
   // this is a map of ServletContexts to BeanGetters
  private static Map beanfactorymap = Collections.synchronizedMap(new HashMap());
  
  // Return a BeanGetter, either by looking in the static map, or if no entry
  // found, looking for a civilized WebApplicationContext.
  public static BeanGetter getBeanFactory(ServletContext context) {
//    if (context == null) context = getServletState().context;
    BeanGetter togo = (BeanGetter) beanfactorymap.get(context);
    if (togo == null) {
      final WebApplicationContext wac = WebApplicationContextUtils
              .getWebApplicationContext(context);
      if (wac != null) {
        togo = new BeanGetter() {
          public Object getBean(String beanname) {
            return wac.getBean(beanname);
          }}; 
      }
    }
    return togo;
  }
  
  public static void registerBeanFactory(ServletContext context, BeanGetter lbf) {
    beanfactorymap.put(context, lbf);
  }
  
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
  
  // Migrate to this method preferentially.
  public static String getBaseURL2(HttpServletRequest hsr) {
    String requestURL = hsr.getRequestURL().toString();
    String extrapath = hsr.getPathInfo();
    if (extrapath == null || extrapath == "") {
      return requestURL;
    }
    int embedpoint = requestURL.lastIndexOf(extrapath);
    if (embedpoint == -1) {
      throw new UniversalRuntimeException("Cannot locate path info of "
          + extrapath + " within request URL of " + requestURL);
    }
    return requestURL.substring(0, embedpoint);
  }
//  
//  public static String getExtraPath(HttpServletRequest hsr) {
//    String baseURL = getBaseURL(hsr);
//    return hsr.getRequestURL().substring(baseURL.length());
//  }

}
