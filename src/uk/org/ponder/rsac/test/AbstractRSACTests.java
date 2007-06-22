/*
 * Created on 22 Jun 2007
 */
package uk.org.ponder.rsac.test;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

public abstract class AbstractRSACTests extends AbstractDependencyInjectionSpringContextTests {
  public abstract String[] getRequestConfigLocations();
  
  protected ConfigurableApplicationContext loadContextLocations(String[] locations) {
    final LocalRSACResourceLocator resourceLocator = new LocalRSACResourceLocator();
    resourceLocator.setConfigLocations(getRequestConfigLocations());
    
    final ConfigurableApplicationContext cac = new ClassPathXmlApplicationContext(locations, false) {
      protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)  {
        resourceLocator.setApplicationContext(this);
        beanFactory.registerSingleton("RSACResourceLocator", resourceLocator);
      }
    };
  
    cac.refresh();
    return cac;
  }
}
