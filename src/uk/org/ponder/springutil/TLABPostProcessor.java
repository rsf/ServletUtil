/*
 * Created on 7 Aug 2006
 */
package uk.org.ponder.springutil;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class TLABPostProcessor implements BeanPostProcessor, ApplicationContextAware {
  private ApplicationContext applicationContext;

  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
    String[] viewbeans = applicationContext
      .getBeanNamesForType(StaticTLAB.class, false, false);
  }
  
  public Object postProcessAfterInitialization(Object bean, String beanName) {
    return bean;
  }

  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
   
    // TODO Auto-generated method stub
    return null;
  }


}
