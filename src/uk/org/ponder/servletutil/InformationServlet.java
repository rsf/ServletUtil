/*
 * Created on Dec 9, 2004
 */
package uk.org.ponder.servletutil;

import java.io.IOException;
import java.util.logging.Level;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import uk.org.ponder.errorutil.ErrorStateEntry;
import uk.org.ponder.errorutil.ThreadErrorState;
import uk.org.ponder.saxalizer.XMLProvider;
import uk.org.ponder.transaction.TransactionThreadMap;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.UniversalRuntimeException;
import uk.org.ponder.webapputil.BeanGetter;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *  
 */
public class InformationServlet extends HttpServlet {
  private InformationHandlerRoot handlerroot;

  public void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    service(req, res);
  }

  public void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    service(req, res);
  }

  private String baseURL;

  public void service(HttpServletRequest req, HttpServletResponse res) {
    try {
      // this is inserted to work around a Sakai component manager bug - 
      // request for ihr below triggers recreation of all beans although they
      // have already been created, in particular sakaibeanroot.
      //ServletUtil.setServletState(getServletContext(), req, res);
      if (handlerroot == null) {
        BeanGetter beangetter = ServletUtil.getBeanFactory(getServletContext());
        handlerroot = (InformationHandlerRoot) beangetter
            .getBean("informationhandlerroot");
        baseURL = ServletUtil.getBaseURL(req);
      }
      String requestURI = req.getRequestURL().toString();
      String handlerID = requestURI.substring(baseURL.length());
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
          errorid = handlerroot.getIDGenerator().generateID();
          t = t1;
        }
        ErrorStateEntry ese = ThreadErrorState.getErrorState();
        if (ese != null) {
          errorid = ese.tokenid;
          for (int i = 0; i < ese.messageSize(); ++i) {
            extraerrors += ese.messageAt(i).message
                + ese.messageAt(i).exceptionclass;
          }
        }
        if (errorid != null) {
          ErrorObject err = new ErrorObject("Error ID " + errorid
              + " processing request " + req.getRequestURI(), handlerID, t);
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
//      ServletUtil.getServletState().clear();
    }
  }
}