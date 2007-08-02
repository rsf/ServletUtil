/*
 * Created on 2 Aug 2007
 */
package uk.org.ponder.springutil;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import uk.org.ponder.beanutil.BeanLocator;

/** Adapts the Spring BeanFactory interface to the PUC BeanLocator interface **/

public class BeanFactoryBeanLocator implements BeanLocator, BeanFactoryAware {
  private BeanFactory beanFactory;
  public void setBeanFactory(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }
  public Object locateBean(String name) {
    return beanFactory.containsBean(name) ? beanFactory.getBean(name) : null;
  }

}
