/*
 * Created on Sep 21, 2005
 */
package uk.org.ponder.springutil;

import javax.servlet.ServletRequest;

import uk.org.ponder.beanutil.BeanLocator;

/**
 * @author andrew, Antranig
 * 
 */
public class RSACUtils {
  public static final String REQUEST_SCOPE_APP_CONTEXT_ATTRIBUTE = "requestScopeApplicationContext";

  public static void setRequestApplicationContext(
      ServletRequest request, BeanLocator context) {
    request.setAttribute(REQUEST_SCOPE_APP_CONTEXT_ATTRIBUTE, context);
  }

  public static BeanLocator getRequestApplicationContext(
      ServletRequest request) {
    return (BeanLocator) request
        .getAttribute(REQUEST_SCOPE_APP_CONTEXT_ATTRIBUTE);
  }

  public static void removeRequestApplicationContext(ServletRequest request) {
    request.removeAttribute(REQUEST_SCOPE_APP_CONTEXT_ATTRIBUTE);
  }
}
