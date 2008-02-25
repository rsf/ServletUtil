/*
 * Created on Sep 21, 2005
 */
package uk.org.ponder.rsac.servlet;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import uk.org.ponder.rsac.RSACBeanLocator;
import uk.org.ponder.util.Logger;

/**
 * The RSAC initiator, packaged as a standard Servlet filter. Expects the standard
 * Spring WebApplicationContext to have been already initialised (presumably by a 
 * listener), and will wrap the servlet request invocation in the standard start/stop
 * of the RSAC thread-bound context.
 * 
 * @author andrew
 * 
 */
public class RSACFilter implements Filter {

  private RSACBeanLocator rsacbg;

  public void init(FilterConfig filterConfig)  {
    WebApplicationContext wac = WebApplicationContextUtils
        .getWebApplicationContext(filterConfig.getServletContext());
    Exception exception = null;
    try {
      rsacbg = (RSACBeanLocator) wac.getBean("RSACBeanLocator");
    }
    catch (Exception e) {
      exception = e;
    }
    if (rsacbg == null || exception != null) {
      Logger.log.fatal("Unable to load RSACBeanLocator from application context", exception);
    }
  }
  
  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) {
    try {
      RSACUtils.startServletRequest((HttpServletRequest)request, (HttpServletResponse) response, 
          rsacbg, RSACUtils.HTTP_SERVLET_FACTORY);
      chain.doFilter(request, response);
    }
    catch (Exception e) {
      // Catch and log this here because Tomcat's stack rendering is non-standard and crummy.
      Logger.log.error("Error servicing RSAC request: ", e);
    }
    finally {
      rsacbg.endRequest();
    }
  }

  public void destroy() {
    this.rsacbg = null;
  }

}