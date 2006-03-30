/*
 * Created on Dec 9, 2004
 */
package uk.org.ponder.servletutil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import uk.org.ponder.errorutil.ErrorStateEntry;
import uk.org.ponder.errorutil.TargettedMessageList;
import uk.org.ponder.errorutil.ThreadErrorState;
import uk.org.ponder.hashutil.EighteenIDGenerator;
import uk.org.ponder.rsac.RSACBeanLocator;
import uk.org.ponder.rsac.servlet.RSACUtils;
import uk.org.ponder.saxalizer.XMLProvider;
import uk.org.ponder.transaction.TransactionThreadMap;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.UniversalRuntimeException;
import uk.org.ponder.webapputil.ErrorObject;

/**
 * InformationServlet represents a generalised web service handler.
 * The portion of the URL after the servlet mapping is used as an index
 * into a table of InformationHandler objects, which are handed the
 * deserialised argument decoded from the request body. These return an
 * object which is serialized as the response.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *  
 */
public class InformationServlet extends HttpServlet {
  private InformationHandlerRoot handlerroot;

  private RSACBeanLocator rsacbeanlocator;

  public void doGet(HttpServletRequest req, HttpServletResponse res) {
    service(req, res);
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res) {
    service(req, res);
  }

  public void init(ServletConfig config) {
    ServletContext sc = config.getServletContext();
    Logger.log.warn("ReasonableSpringServlet starting up for context "
        + sc.getRealPath(""));
    WebApplicationContext wac = WebApplicationContextUtils.getWebApplicationContext(sc);
    rsacbeanlocator = (RSACBeanLocator) wac.getBean("RSACBeanLocator");
    handlerroot = (InformationHandlerRoot) wac.getBean("informationHandlerRoot");
  }
 
  private EighteenIDGenerator idgenerator = new EighteenIDGenerator();

  public void service(HttpServletRequest req, HttpServletResponse res) {
    ThreadErrorState.beginRequest();
    RSACUtils.startServletRequest(req, res, 
        rsacbeanlocator, RSACUtils.HTTP_SERVLET_FACTORY);
    try {
      String baseURL2 = ServletUtil.getBaseURL2(req);
      Logger.log.info("baseURL2: " + baseURL2);
      String requestURI = req.getRequestURL().toString();
      int lastslashpos = requestURI.lastIndexOf('/');
      
      //String handlerID = requestURI.substring(baseURL.length());
      String handlerID = requestURI.substring(lastslashpos + 1);
      InformationHandler handler = handlerroot.getHandler(handlerID);
      if (handler == null) {
        String errmess = "The request handler " + handlerID
            + " could not be found to service the request "
            + req.getRequestURI();
        Logger.log.warn(errmess);
        res.sendError(HttpServletResponse.SC_NOT_FOUND, errmess);
      }
      else {
        XMLProvider xmlprovider = handlerroot.getXMLProvider();
        Throwable t = null;
        String errorid = null;
        String extraerrors = "";
        try {
          //String input = StreamCopier.streamToString(req.getInputStream());
          Object arg = xmlprovider.readXML(null, req.getInputStream());
          Object togo = handler.handleRequest(arg);
          if (togo != null) {
            xmlprovider.writeXML(togo, res.getOutputStream());
            String debugstring = xmlprovider.toString(togo);
           Logger.log.info(
                "InformationServlet returning response:\n" + debugstring);
          }
          else {

          }
        }
        catch (Throwable t1) {
          Logger.log.warn("Error handling request: ", t1);
          errorid = handlerroot.getIDGenerator().generateID();
          t = t1;
        }
        ErrorStateEntry threaderrors = ThreadErrorState.getErrorState();
        TargettedMessageList errors = null;
        
        if (threaderrors == null) {
          Logger.log.error("Got null ESE from ThreadLocal");
          errors = new TargettedMessageList();
        }
        else {
          errors = threaderrors.errors;
        }
        if (errors.size() > 0) {
          for (int i = 0; i < errors.size(); ++i) {
            extraerrors += errors.messageAt(i).messagecode
                + errors.messageAt(i).exceptionclass;
          }
        }
        if (errorid != null) {
          ErrorObject err = new ErrorObject("Error ID " + errorid
              + " processing request " + req.getRequestURI(), handlerID, t);
          // TODO: This is probably not quite right - what if an error occurs
          // partway through writing the output?
          xmlprovider.writeXML(err, res.getOutputStream());
          Logger.log.warn("Error ID " + errorid
              + " processing request " + req.getRequestURL(), t);
        }
      } // end if there is a handler
      //TODO: global registry of transactions
      TransactionThreadMap.assertAllTransactionsConcluded();
    }
    catch (Throwable t) {
      Logger.log.fatal("Completely fatal exception handling request " + 
          req.getRequestURI(), t);
      throw UniversalRuntimeException.accumulate(t,
          "Fatal exception in InformationServlet");
    }
    finally {
      rsacbeanlocator.endRequest();
//      ServletUtil.getServletState().clear();
    }
  }
}