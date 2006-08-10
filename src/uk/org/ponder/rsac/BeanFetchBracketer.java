/*
 * Created on 10-Mar-2006
 */
package uk.org.ponder.rsac;

/** A definition read by RSAC that it will use to operate a RunnableWrapper
 * surrounding the fetch and initialization of a target request scope bean.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class BeanFetchBracketer {
  /** The name of the request scope bean whose fetch is to be bracketed */
  private String targetBeanName;
  /** The name of a bean (of type RunnableWrapper), or else an actual bean
   *  that will be used to perform the bracketing. 
   */ 
  private Object wrappingBean;
   
  public void setTargetBeanName(String targetBeanName) {
    this.targetBeanName = targetBeanName;
  }
  public String getTargetBeanName() {
    return targetBeanName;
  }
  /** Either a RunnableWrapper, or an EL expression where such can be
   * found.
   */
  public void setWrappingBean(Object wrappingBeanName) {
    this.wrappingBean = wrappingBeanName;
  }
  public Object getWrappingBean() {
    return wrappingBean;
  }
}
