/*
 * Created on Dec 24, 2004
 */
package uk.org.ponder.servletutil;


/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public interface WebServiceDispatcher {
  public String getName();
  public String getRemoteURLBase();
  public void handleRequest(ServletForwardPackage forwardpackage);
}
