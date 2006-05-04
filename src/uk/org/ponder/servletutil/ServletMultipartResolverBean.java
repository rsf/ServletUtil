/*
 * Created on 04-May-2006
 */
package uk.org.ponder.servletutil;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartResolver;

import uk.org.ponder.springutil.MultipartResolverBean;

/** A multipart file upload resolver suitable for use in a Servlet
 * environment. Simply express a dependency, and make a call to 
 * "getMultipartMap" from within a suitable request context.
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */

public class ServletMultipartResolverBean implements MultipartResolverBean {
  private HttpServletRequest request;
  private MultipartResolver resolver;

  public void setHttpServletRequest(HttpServletRequest request) {
    this.request = request;
  }
  
  public void setMultipartResolver(MultipartResolver resolver) {
    this.resolver = resolver;
  }
  
  public Map getMultipartMap() throws MultipartException {
    if (resolver.isMultipart(request)) {
      return resolver.resolveMultipart(request).getFileMap();
    }
    else return null;
  }
}
