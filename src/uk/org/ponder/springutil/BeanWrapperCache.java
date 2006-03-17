/*
 * Created on Aug 2, 2005
 */
package uk.org.ponder.springutil;

import java.util.HashMap;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/** An obsolete attempt to remove the enormous expense of constructing
 * Spring BeanWrapperImpl objects.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class BeanWrapperCache implements ApplicationContextAware {

  private HashMap wrapperforpath = new HashMap();
  private BeanFactory factory;
  
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    factory = applicationContext;
  }
  
  public BeanWrapper wrapperForPath(String path) {
    BeanWrapper wrapper = (BeanWrapper) wrapperforpath.get(path);
    if (wrapper == null) {
      Object bean = factory.getBean(path);
      wrapper = new BeanWrapperImpl(bean, path, factory);
      wrapperforpath.put(path, wrapper);
    }
    return wrapper;
  }

}
