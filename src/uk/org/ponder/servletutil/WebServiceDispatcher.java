/*
 * Created on Dec 24, 2004
 */
package uk.org.ponder.servletutil;

import uk.org.ponder.webapputil.ConsumerInfo;


/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public interface WebServiceDispatcher {
  public static final String USERID_PARAMETER = "userID";
  public static final String VIEW_TEMPLATE_PARAMETER = "viewtemplate";
  
  public static final String CONSUMERINFO_PARAMETER = "consumerinfo";
  
  //public static final String CONSUMERID_PARAMETER = "consumerID";
  //public static final String CONSUMERTYPE_PARAMETER = "consumertype";
  //public static final String INFORMATIONBASE_PARAMETER = "informationurl";

  public String getName();
  public String getRemoteURLBase();

  public void setConsumerInfo(ConsumerInfo consumerinfo);
  public void handleRequest(ServletForwardPackage forwardpackage);
  public WebServiceDispatcher copy();
}
