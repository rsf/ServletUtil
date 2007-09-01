/*
 * Created on Nov 20, 2005
 */
package uk.org.ponder.rsac.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import uk.org.ponder.rsac.RSACBeanLocatorImpl;
import uk.org.ponder.servletutil.ServletRequestWrapper;

public class RSACServletRequestWrapper implements ServletRequestWrapper{
  private RSACBeanLocatorImpl rsacbl;

  public RSACServletRequestWrapper(RSACBeanLocatorImpl rsacbl) {
    this.rsacbl = rsacbl;
  }
  public void startRequest(HttpServletRequest request, HttpServletResponse response) {
    RSACUtils.startServletRequest(request, response, rsacbl, RSACUtils.HTTP_SERVLET_FACTORY);    
  }

  public void endRequest() {
    rsacbl.endRequest();
  }

}
