/*
 * Created on Nov 20, 2005
 */
package uk.org.ponder.rsac.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class StaticHttpServletFactory implements HttpServletFactory {
  private HttpServletRequest request;
  private HttpServletResponse response;
  
  public void setHttpServletRequest(HttpServletRequest request) {
    this.request = request;
  }
  public HttpServletRequest getHttpServletRequest() {
    return request;
  }
  public void setHttpServletResponse(HttpServletResponse response) {
    this.response = response;
  }
  public HttpServletResponse getHttpServletResponse() {
    return response;
  }
}
