/*
 * Created on 24 Nov 2006
 */
package uk.org.ponder.servletutil;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ContextReportingServlet extends HttpServlet {
  public static String CONTEXT_REQUEST_ATTRIBUTE = "ponder-servletutil-context";

  protected void service(HttpServletRequest req, HttpServletResponse resp) {
    req.setAttribute(CONTEXT_REQUEST_ATTRIBUTE, getServletContext());
  }
}
