/*
 * Created on Jan 6, 2005
 */
package uk.org.ponder.servletutil;

import uk.org.ponder.saxalizer.XMLProvider;

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
public class DirectURLDispatcher implements WebServiceDispatcher {

  // The name of this dispatcher within the collection
  String name;
  
  Object clientrequestinfo;
  String remoteurlbase;
  
  public static void copyBase(DirectURLDispatcher source, DirectURLDispatcher dest) {
    dest.name = source.name;
    dest.remoteurlbase = source.remoteurlbase;
    dest.xmlprovider = source.xmlprovider;
  }
  
  public WebServiceDispatcher copy() {
    DirectURLDispatcher togo = new DirectURLDispatcher();
    copyBase(this, togo);
    return togo;
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
  
  public void setClientRequestInfo(Object clientrequestinfo) {
    this.clientrequestinfo = clientrequestinfo;
  }
  
  public void handleRequest(ServletForwardPackage forwardpackage) {
    //No - this is WRONG! the resourceurlbase should be configured statically
    //in consumerinfo.xml, and in general even left blank if the server's
    //own URL is globally usable.
    //consumerinfo.resourceurlbase = forwardpackage.localurlbase;
    
    String clientrequestinfostring = xmlprovider.toString(clientrequestinfo);
    forwardpackage.addParameter(WebServiceDispatcher.REQUEST_INFO_PARAMETER, 
        clientrequestinfostring);
    
    forwardpackage.setUnwrapRedirect(true);
    forwardpackage.dispatchTo(forwardpackage.targeturl);
  }


}
