/*
 * Created on Aug 1, 2005
 */
package uk.org.ponder.servletutil;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class ServletState {
  // Inclusion of request and response as thread-local state is a potentially
  // dubious design - this was primarily motivated by being able to abstract
  // the action of issuing a redirect, which for JSF is performed on ExternalContext,
  // for plain servlets on HSResponse, neither of which should be something known
  // to issuing code. This should be protected by appropriately restricting
  // knowledge level of this package. This is potentially doubly dubious since
  // HSR has STATE. In general do not use this state if at all possible,
  // preferentially 
    public ServletContext context;
    public HttpServletRequest request;
    public HttpServletResponse response;
    public void clear() {
      context = null;
      request = null;
      response = null;
    }
  private static ThreadLocal contextstash = new ThreadLocal() {
    public Object initialValue() {
      return new ServletState();
    }
  };
//  public static void setServletContext(ServletContext toset) {
//    contextstash.set(toset);
//  }
  public static void setServletState(ServletContext context, HttpServletRequest request,
      HttpServletResponse response) {
    ServletState state = getServletState();
    state.context = context;
    state.request = request;
    state.response = response;
  }
  public static ServletState getServletState() {
    return (ServletState) contextstash.get();
  }
  
}
