/*
 * Created on Jan 6, 2005
 */
package uk.org.ponder.servletutil;

import uk.org.ponder.saxalizer.XMLProvider;
import uk.org.ponder.webapputil.ConsumerInfo;

/**
 * An endpoint handler of requests passing through WebAppTool and 
 * WebAppToolSink that works in a canonical way - it dispatches
 * "like-for-like" in that HTTP GETs and POSTs originating from the
 * client are mapped onto the same, and the remote servlet is
 * provided with enough context to allow it to compute ultimate
 * URLs by itself, obviating the need for rewriting.
 * <p>Individual tool handlers override the <code>adjustRequest</code>
 * method which is provided with a freely writeable parameter map
 * object into which should be put any extra parameters required 
 * to deal with the request (e.g. authentication and url base).
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */
public abstract class DirectURLDispatcher implements WebServiceDispatcher {
//  public static final String CONSUMER_URL_BASE = "consumerURLbase";
//  public static final String CONSUMER_RESOURCE_URL_BASE = "consumerresourceURLbase";

  // The name of this dispatcher within the collection
  String name;
  
  protected ConsumerInfo consumerinfo;
  String remoteurlbase;
  
  public static void copyBase(DirectURLDispatcher source, DirectURLDispatcher dest) {
    dest.name = source.name;
    dest.remoteurlbase = source.remoteurlbase;
    dest.consumerinfo = source.consumerinfo;
    dest.xmlprovider = source.xmlprovider;
  }
  
  private XMLProvider xmlprovider;
  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
  
  public String getRemoteURLBase() {
    return remoteurlbase;
  }
/** Set the base URL of the remote servlet, including trailing slash */
  public void setRemoteURLBase(String urlbase) {
    this.remoteurlbase = urlbase;
  }
  
  public void setXMLProvider(XMLProvider xmlprovider) {
    this.xmlprovider = xmlprovider;
  }
  
  public void setConsumerInfo(ConsumerInfo consumerinfo) {
    this.consumerinfo = consumerinfo;
  }
  
  public void handleRequest(ServletForwardPackage forwardpackage) {
    String consumerinfostring = xmlprovider.toString(consumerinfo);
    forwardpackage.addParameter(CONSUMERINFO_PARAMETER, consumerinfostring);
    
    consumerinfo.urlbase = forwardpackage.localurlbase;
    consumerinfo.resourceurlbase = forwardpackage.localurlbase;
    
    forwardpackage.addParameter(CONSUMERID_PARAMETER, consumerinfo.consumerid);
    forwardpackage.setUnwrapRedirect(true);
    forwardpackage.dispatchTo(forwardpackage.targeturl);
  }

}
