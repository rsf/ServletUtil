/*
 * Created on 21 Nov 2006
 */
package uk.org.ponder.rsac.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HttpServletFactory {

  public void setHttpServletRequest(HttpServletRequest request);

  public HttpServletRequest getHttpServletRequest();

  public void setHttpServletResponse(HttpServletResponse response);

  public HttpServletResponse getHttpServletResponse();
}