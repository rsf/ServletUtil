/*
 * Created on Sep 21, 2005
 */
package uk.org.ponder.springutil;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import uk.org.ponder.servletutil.RequestPostProcessor;

/**
 * @author andrew
 * 
 */
public class RSACFilter implements Filter {

  private RSACBeanGetter rsacbg;

  public void init(FilterConfig filterConfig)  {
    WebApplicationContext wac = WebApplicationContextUtils
        .getWebApplicationContext(filterConfig.getServletContext());
    rsacbg = (RSACBeanGetter) wac.getBean("rsacbeangetter");
  }

  public void doFilter(ServletRequest request, ServletResponse response,
      FilterChain chain) throws IOException, ServletException {
    try {
      RequestPostProcessor rpp = new RequestPostProcessor(
          (HttpServletRequest) request, (HttpServletResponse) response);
      rsacbg.beginRequest();
      rsacbg.addPostProcessor(rpp);
      RSACUtils.setRequestApplicationContext(request, rsacbg);
      chain.doFilter(request, response);
    }
    finally {
      rsacbg.endRequest();
    }
  }

  public void destroy() {
    this.rsacbg = null;
  }

}