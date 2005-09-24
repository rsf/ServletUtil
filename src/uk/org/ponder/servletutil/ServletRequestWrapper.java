/*
 * Created on Sep 23, 2005
 */
package uk.org.ponder.servletutil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface ServletRequestWrapper {
  public void startRequest(HttpServletRequest request, HttpServletResponse response);
  public void endRequest();
}
