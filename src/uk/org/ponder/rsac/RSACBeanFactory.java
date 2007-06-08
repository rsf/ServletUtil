/*
 * Created on 5 Jun 2007
 */
package uk.org.ponder.rsac;


import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanNotOfRequiredTypeException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;

import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.util.Logger;

/**
 * Adapts the per-request RSAC container to a standard Spring BeanFactory,
 * primarily for the purpose of beans which report themselves BeanFactoryAware.
 * 
 * Note that any bean requiring ApplicationContextAware will actually receive
 * the main application-scope ApplicationContext rather than a request-scope
 * container.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */

public class RSACBeanFactory implements BeanFactory {
  private BeanLocator locator;
  private RSACBeanLocator rsacbl;

  public RSACBeanFactory(RSACBeanLocator rsacbl, BeanLocator bl) {
    this.locator = bl;
    this.rsacbl = rsacbl;
  }

  public boolean containsBean(String name) {
    return rsacbl.getBlankContext().containsBean(name);
  }

  public String[] getAliases(String name) throws NoSuchBeanDefinitionException {
    // Aliases are not supported in RSAC
    return null;
  }

  public Object getBean(String name) throws BeansException {
    return locator.locateBean(name);
  }

  public Object getBean(String name, Class requiredType) throws BeansException {
    Object bean = locator.locateBean(name);
    if (requiredType != null && !requiredType.isAssignableFrom(bean.getClass())) {
      throw new BeanNotOfRequiredTypeException(name, requiredType, bean
          .getClass());
    }
    return bean;
  }

  // This peculiar method appeared in Spring 2.0
  public boolean isTypeMatch(String name, Class targetType)
      throws NoSuchBeanDefinitionException {
    Object bean = getBean(name);
    Class typeToMatch = (targetType != null ? targetType
        : Object.class);
    return typeToMatch.isAssignableFrom(bean.getClass());
  }

  public Class getType(String name) throws NoSuchBeanDefinitionException {
    Class staticclazz = rsacbl.getBeanClass(name);
    if (staticclazz == null) {
      Object bean = getBean(name);
      return bean.getClass();
    }
    else
      return staticclazz;
  }

  public boolean isSingleton(String name) throws NoSuchBeanDefinitionException {
    RSACBeanInfo rbi = (RSACBeanInfo) rsacbl.getRBIMap().get(name);
    if (rbi == null) {
      throw new NoSuchBeanDefinitionException(name, "No such bean");
    }
    else
      return rbi.issingleton;
  }

  public boolean isPrototype(String name) throws NoSuchBeanDefinitionException {
    return !isSingleton(name);
  }

}
