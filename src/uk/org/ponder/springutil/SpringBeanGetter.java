/*
 * Created on Aug 4, 2005
 */
package uk.org.ponder.springutil;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

import uk.org.ponder.beanutil.BeanGetter;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class SpringBeanGetter implements BeanGetter, BeanFactoryAware {
  private BeanFactory factory;
  public SpringBeanGetter() {}
  public SpringBeanGetter(BeanFactory factory) {
    this.factory = factory;
  }
  public Object getBean(String beanname) {
   return factory.getBean(beanname);
  }
  public void setBeanFactory(BeanFactory factory) throws BeansException {
    this.factory = factory;
  }
  
}
