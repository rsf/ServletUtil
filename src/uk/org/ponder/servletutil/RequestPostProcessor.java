/* This class lamentably ripped off from Andrew Thornton. 
 * Created on Sep 18, 2005
 */
package uk.org.ponder.servletutil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class RequestPostProcessor implements BeanPostProcessor {

  /**
   * The current ServletRequest
   */
  private HttpServletRequest request = null;

  /**
   * The current ServletResponse
   */
  private HttpServletResponse response = null;

  /**
   * Constructor that takes the current ServletRequest and ServletResponse
   * 
   * @param request
   *          the current ServletRequest
   * @param response
   *          the current ServletResponse
   */
  public RequestPostProcessor(HttpServletRequest request,
      HttpServletResponse response) {
    this.request = request;
    this.response = response;
  }

  /**
   * Empty constructor that can only be accessed from with this package
   * 
   */
  protected RequestPostProcessor() {

  }

  /**
   * Sets the current servlet request and servlet response on beans that are
   * aware
   * 
   * @see ServletRequestAware and
   * @see ServletResponseAware
   * @param bean
   *          bean to post process
   * @param beanName
   *          name of the bean
   * @return the bean
   */
  public Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {
    if (bean instanceof HttpServletRequestAware) {
      ((HttpServletRequestAware) bean).setHttpServletRequest(request);
    }

    if (bean instanceof HttpServletResponseAware) {
      ((HttpServletResponseAware) bean).setHttpServletResponse(response);
    }
    return bean;
  }

  /**
   * This does nothing and will simply return the bean.
   * 
   * @param bean
   *          bean to post process
   * @param beanName
   *          name of the bean
   * @return the bean
   */
  public Object postProcessAfterInitialization(Object bean, String beanName)
      throws BeansException {
    // Do nothing!
    return bean;
  }

  /**
   * Will contextualise the postProcessor to the given request and response
   * 
   * @param request
   * @param response
   */
  public void contextualise(HttpServletRequest request,
      HttpServletResponse response) {
    this.request = request;
    this.response = response;
  }

  /**
   * Will reset the post processor, allowing it to be returned to the pool
   * 
   */
  public void reset() {
    this.request = null;
    this.response = null;
  }

}
