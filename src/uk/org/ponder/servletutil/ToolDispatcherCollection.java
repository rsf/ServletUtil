/*
 * Created on Jan 6, 2005
 */
package uk.org.ponder.servletutil;

import java.util.HashMap;
import java.util.List;


/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class ToolDispatcherCollection {
  HashMap tools = new HashMap();
  public void setToolDispatchers(List toollist) {
    for (int i = 0; i < toollist.size(); ++ i) {
      WebServiceDispatcher dispatcher = (WebServiceDispatcher) toollist.get(i);
      tools.put(dispatcher.getName(), dispatcher);
    }
  }
  
  public WebServiceDispatcher getDispatcher(String toolname) {
    return (WebServiceDispatcher) tools.get(toolname);
  }
}
