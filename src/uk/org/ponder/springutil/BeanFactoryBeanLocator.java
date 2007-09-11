/*
 * Created on 2 Aug 2007
 */
package uk.org.ponder.springutil;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import uk.org.ponder.beanutil.BeanLocator;

/** Adapts the Spring BeanFactory interface to the PUC BeanLocator interface **/

public class BeanFactoryBeanLocator implements BeanLocator, BeanFactoryAware {
  private BeanFactory beanfactory;
  public BeanFactoryBeanLocator() {}
  
  public BeanFactoryBeanLocator(BeanFactory beanfactory) {
    this.beanfactory = beanfactory;
  }
  
  public void setBeanFactory(BeanFactory beanfactory) {
    this.beanfactory = beanfactory;
  }
  
  public Object locateBean(String name) {
    return beanfactory.containsBean(name) ? beanfactory.getBean(name) : null;
  }

}
