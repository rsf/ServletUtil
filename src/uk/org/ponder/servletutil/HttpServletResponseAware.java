/*
 * Created on Sep 18, 2005
 */
package uk.org.ponder.servletutil;

import javax.servlet.http.HttpServletResponse;

public interface HttpServletResponseAware {

  void setHttpServletResponse(HttpServletResponse response);

}
