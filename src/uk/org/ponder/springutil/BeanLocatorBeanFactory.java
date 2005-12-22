/*
 * Created on Nov 28, 2005
 */
package uk.org.ponder.springutil;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import uk.org.ponder.beanutil.BeanLocator;

/** A very simple adapter from BeanLocator to BeanFactory. Assumes that all
 * beans are singletons, and will actually init any bean whose type is 
 * requested.
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */

public class BeanLocatorBeanFactory implements BeanFactory {
  private BeanLocator locator;

  public BeanLocatorBeanFactory() {}
  
  public BeanLocatorBeanFactory(BeanLocator locator) {
    setBeanLocator(locator);
  }
  
  public void setBeanLocator(BeanLocator locator) {
    this.locator = locator;
  }
  public Object getBean(String name) throws BeansException {
    return locator.locateBean(name);
  }

  public Object getBean(String name, Class requiredType) throws BeansException {
    Object bean = locator.locateBean(name);
    if (requiredType != null && !requiredType.isAssignableFrom(bean.getClass())) {
        throw new BeanNotOfRequiredTypeException(name, requiredType, bean.getClass());
    }
    return bean;
  }

  public boolean containsBean(String name) {
    return locator.locateBean(name) != null;
  }

  public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
    Object bean = locator.locateBean(name);
    if (bean == null) {
      throw new NoSuchBeanDefinitionException(name, "No such bean");
    }
    // ALL RSAC beans are singletons. It would be hard for them to have a 
    // lifetime much shorter...
    return true;
  }

  public Class getType(String name) throws NoSuchBeanDefinitionException {
    Object bean = locator.locateBean(name);
    if (bean == null) {
      throw new NoSuchBeanDefinitionException(name, "No such bean");
    }
    return locator.locateBean(name).getClass();
  }

  public String[] getAliases(String name) throws NoSuchBeanDefinitionException {
    // TODO Auto-generated method stub
    return null;
  }

}
