/*
 * Created on Dec 9, 2004
 */
package uk.org.ponder.servletutil;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public interface InformationHandler {
  public String getName();
  public Object handleRequest(Object argument);
}
