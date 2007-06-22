/*
 * Created on 22 Jun 2007
 */
package uk.org.ponder.rsac.test;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.AbstractDependencyInjectionSpringContextTests;

import uk.org.ponder.rsac.RSACBeanLocator;

public abstract class AbstractRSACTests extends
    AbstractDependencyInjectionSpringContextTests {
  private RSACBeanLocator rsacbl;
  public abstract String[] getRequestConfigLocations();

  protected ConfigurableApplicationContext loadContextLocations(
      String[] locations) {
    final LocalRSACResourceLocator resourceLocator = new LocalRSACResourceLocator();
    resourceLocator.setConfigLocations(getRequestConfigLocations());

    final ConfigurableApplicationContext cac = new ClassPathXmlApplicationContext(
        locations, false) {
      protected void postProcessBeanFactory(
          ConfigurableListableBeanFactory beanFactory) {
        resourceLocator.setApplicationContext(this);
        beanFactory.registerSingleton("RSACResourceLocator", resourceLocator);
      }
    };

    cac.refresh();
    return cac;
  }

  public RSACBeanLocator getRSACBeanLocator() {
    return rsacbl;
  }
  
  protected void onSetUp() throws Exception {
    rsacbl = (RSACBeanLocator) applicationContext.getBean("RSACBeanLocator");
    rsacbl.startRequest();
  }

  protected void onTearDown() throws Exception {
    rsacbl.endRequest();
  }
}
