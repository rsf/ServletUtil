/*
 * Created on Dec 9, 2004
 */
package uk.org.ponder.servletutil;

/**
 * The interface exposed by a "lite Web Service" as stored in the 
 * InformationHandlerRoot and serviced by the InformationServlet.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */
public interface InformationHandler {
  public String getName();
  public Object handleRequest(Object argument);
}
