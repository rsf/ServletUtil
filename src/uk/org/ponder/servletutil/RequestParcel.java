/*
 * Created on Dec 23, 2004
 */
package uk.org.ponder.servletutil;

import java.util.List;

import uk.org.ponder.saxalizer.DeSAXalizable;
import uk.org.ponder.saxalizer.SAXAccessMethodSpec;
import uk.org.ponder.saxalizer.SAXalizable;

/**
 * Encapsulates an RPC-like method call structure.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class RequestParcel implements SAXalizable, DeSAXalizable {
  public String methodname;
  public List arguments;
  public SAXAccessMethodSpec[] getSAXSetMethods() {
    // TODO Auto-generated method stub
    return null;
  }
  public SAXAccessMethodSpec[] getSAXGetMethods() {
    // TODO Auto-generated method stub
    return null;
  }
}
