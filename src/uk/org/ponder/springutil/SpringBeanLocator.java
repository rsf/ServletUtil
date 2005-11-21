/*
 * Created on Aug 4, 2005
 */
package uk.org.ponder.springutil;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import uk.org.ponder.beanutil.BeanLocator;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class SpringBeanLocator implements BeanLocator, BeanFactoryAware {
  private BeanFactory factory;
  public SpringBeanLocator() {}
  public SpringBeanLocator(BeanFactory factory) {
    this.factory = factory;
  }
  public Object locateBean(String beanname) {
   return factory.getBean(beanname);
  }
  public void setBeanFactory(BeanFactory factory) throws BeansException {
    this.factory = factory;
  }
  
}
