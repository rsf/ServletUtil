/*
 * Created on Dec 9, 2004
 */
package uk.org.ponder.servletutil;

import java.util.HashMap;
import java.util.List;

import uk.org.ponder.hashutil.IDGenerator;
import uk.org.ponder.saxalizer.mapping.MappableXMLProvider;
import uk.org.ponder.webapputil.ErrorObject;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class InformationHandlerRoot {
  private HashMap handlermap = new HashMap();
  private MappableXMLProvider xmlprovider;
  private IDGenerator idgenerator;
  
  public void setHandlers(List handlers) {
    for (int i = 0; i < handlers.size(); ++ i) {
      InformationHandler handler = (InformationHandler)handlers.get(i);
      handlermap.put(handler.getName(), handler);
    }
  }
  public InformationHandler getHandler(String name) {
    return (InformationHandler)handlermap.get(name);
  } 
  public void setXMLProvider(MappableXMLProvider xmlprovider) {
    this.xmlprovider = xmlprovider;
    xmlprovider.registerClass("errorobject", ErrorObject.class);
  }
  public MappableXMLProvider getXMLProvider() {
    return xmlprovider;
  }
  public void setIDGenerator(IDGenerator idgenerator) {
    this.idgenerator = idgenerator;
  }
  public IDGenerator getIDGenerator() {
    return idgenerator;
  }
}
