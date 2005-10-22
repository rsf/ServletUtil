/*
 * Created on Sep 18, 2005
 */
package uk.org.ponder.servletutil;

import javax.servlet.http.HttpServletResponse;

/** An interface implemented by request-scope beans who wish to be informed
 * of the servlet response for this request cycle. I don't recommend that
 * anyone actually implement this interface within RSF. This set method will
 * be invoked as part of post-processing of the bean construction. 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */
public interface HttpServletResponseAware {
  void setHttpServletResponse(HttpServletResponse response);
}
