/*
 * Created on Dec 24, 2004
 */
package uk.org.ponder.servletutil;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public interface WebServiceDispatcher {
  public static final String REQUEST_INFO_PARAMETER = "wsdrequest";

  public String getName();
  public String getRemoteURLBase();

  public void setClientRequestInfo(Object clientrequestinfo);
  public void handleRequest(ServletForwardPackage forwardpackage);
  public WebServiceDispatcher copy();
}
