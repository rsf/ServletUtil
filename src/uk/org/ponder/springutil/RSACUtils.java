/*
 * Created on Sep 21, 2005
 */
package uk.org.ponder.springutil;

import javax.servlet.ServletRequest;

import uk.org.ponder.beanutil.BeanGetter;

/**
 * @author andrew, Antranig
 * 
 */
public class RSACUtils {
  public static final String REQUEST_SCOPE_APP_CONTEXT_ATTRIBUTE = "requestScopeApplicationContext";

  public static void setRequestApplicationContext(
      ServletRequest request, BeanGetter context) {
    request.setAttribute(REQUEST_SCOPE_APP_CONTEXT_ATTRIBUTE, context);
  }

  public static BeanGetter getRequestApplicationContext(
      ServletRequest request) {
    return (BeanGetter) request
        .getAttribute(REQUEST_SCOPE_APP_CONTEXT_ATTRIBUTE);
  }

  public static void removeRequestApplicationContext(ServletRequest request) {
    request.removeAttribute(REQUEST_SCOPE_APP_CONTEXT_ATTRIBUTE);
  }
}
