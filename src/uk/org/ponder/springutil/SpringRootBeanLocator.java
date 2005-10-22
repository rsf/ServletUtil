/*
 * Created on Aug 4, 2005
 */
package uk.org.ponder.springutil;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import uk.org.ponder.beanutil.BeanLocator;

/**
 * Converts a Spring BeanFactory to the much narrower BeanLocator interface.
 * Currently disused.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *  
 */
public class SpringRootBeanLocator implements BeanLocator, BeanFactoryAware {
  private BeanFactory factory;

  public SpringRootBeanLocator() {
  }

  public SpringRootBeanLocator(BeanFactory factory) {
    this.factory = factory;
  }

  public Object locateBean(String path) {
    if (path.indexOf('.') != -1) {
      throw new NoSuchBeanDefinitionException(path, "Root path of " + path
          + " is not valid");
    }
    return factory.getBean(path);
  }

  public void setBeanFactory(BeanFactory factory) throws BeansException {
    this.factory = factory;
  }
}