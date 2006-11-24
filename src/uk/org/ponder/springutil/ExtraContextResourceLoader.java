/*
 * Created on 24 Nov 2006
 */
package uk.org.ponder.springutil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequestWrapper;
import javax.servlet.ServletResponseWrapper;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.ServletContextResource;

import uk.org.ponder.util.UniversalRuntimeException;

public class ExtraContextResourceLoader implements ApplicationContextAware,
 ResourceLoader {

  private ApplicationContext applicationContext;
  private WebApplicationContext wac;
  private HttpServletRequest requestproxy;
  private HttpServletResponse responseproxy;
  
  public void setHttpServletRequest(HttpServletRequest request) {
    this.requestproxy = request;
  }

  public void setHttpServletResponse(HttpServletResponse response) {
    this.responseproxy = response;
  }

  
  public Resource getResource(String location) {
    if (wac != null && location.startsWith("/..")) {
      int slashpos = location.indexOf('/', 4);
      String uripath = location.substring(3, slashpos); // include leading /
      String relpath = location.substring(slashpos + 1); 
      ServletContext extracontext = wac.getServletContext().getContext(uripath);
      if (extracontext == null) {
        return getBottledResource(uripath, relpath);
      }
      else return new ServletContextResource(extracontext, relpath);
    }
    else return applicationContext.getResource(location);
  }



  private Resource getBottledResource(String uripath, String relpath) {
    RequestDispatcher rd = wac.getServletContext().getNamedDispatcher(uripath.substring(1));
    final ByteArrayServletOutputStream basos = new ByteArrayServletOutputStream();
    ServletRequestWrapper reqwrapper = new ServletRequestWrapper(requestproxy);
    ServletResponseWrapper reswrapper = new ServletResponseWrapper(responseproxy) {
      public ServletOutputStream getOutputStream() {
        return basos;
      }
    };
    try {
      rd.include(reqwrapper, reswrapper);
    }
    catch (Exception e) {
      throw UniversalRuntimeException.accumulate(e, "Error acquiring resource data for path " + relpath + " for context " + uripath);
    }
   
    return new ByteArrayResource(basos.toByteArray());
  }

  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
    if (applicationContext instanceof WebApplicationContext) {
      this.wac = (WebApplicationContext) applicationContext;
    }
  }

}
