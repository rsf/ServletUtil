/*
 * Created on Sep 18, 2005
 */
package uk.org.ponder.servletutil;

import javax.servlet.http.HttpServletRequest;

public interface HttpServletRequestAware {

  void setHttpServletRequest(HttpServletRequest request);

}
