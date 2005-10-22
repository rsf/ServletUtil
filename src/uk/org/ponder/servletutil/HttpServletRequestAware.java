/*
 * Created on Sep 18, 2005
 */
package uk.org.ponder.servletutil;

import javax.servlet.http.HttpServletRequest;

/** An interface implemented by request-scope beans who wish to be informed
 * of the servlet request giving rise to this request. This set method will
 * be invoked as part of post-processing of the bean construction. 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */
public interface HttpServletRequestAware {
  void setHttpServletRequest(HttpServletRequest request);
}
