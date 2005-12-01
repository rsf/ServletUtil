/*
 * Created on Sep 18, 2005
 */
package uk.org.ponder.rsac;

import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;

import uk.org.ponder.arrayutil.ArrayUtil;
import uk.org.ponder.util.UniversalRuntimeException;

/**
 * This code, grievously stolen from Andrew Thornton, hides away all the
 * unpleasant munging required to obtain a "Generic" Spring application context
 * using standard Spring resource location semantics.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */

public class RSACBeanLocatorFactory implements ApplicationListener {
  private RSACBeanLocator rsacbeanlocator = null;

  /**
   * Creates a RequestScopeAppContextPool from the given config locations and
   * parent Application Context
   * 
   * @param configLocation
   * @param parent
   * @return a new pool
   */
  public static RSACBeanLocator createLocator(String[] configLocations,
      ApplicationContext parent) {
    GenericApplicationContext initialContext = new GenericApplicationContext();
    XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(
        initialContext);

    Resource[] resources = new Resource[0];

    if (configLocations != null) {
      for (int i = 0; i < configLocations.length; ++i) {
        String location = configLocations[i];
        try {
          Resource[] newresources = parent.getResources(location);
          resources = (Resource[]) ArrayUtil.concat(resources, newresources);
        }
        catch (Exception e) {
          throw UniversalRuntimeException.accumulate(e, "ConfigLocation: "
              + location + " causes cannot be loaded ");
        }
      }
    }

    beanDefinitionReader.loadBeanDefinitions(resources);

    initialContext.setParent(parent);

    return new RSACBeanLocator(initialContext);
  }

  public void setRSACResourceLocator(RSACResourceLocator resourcelocator) {
    rsacbeanlocator = createLocator(resourcelocator.getConfigLocation(),
        resourcelocator.getApplicationContext());
  }

  public void onApplicationEvent(ApplicationEvent event) {
    if (event instanceof ContextRefreshedEvent) {
      rsacbeanlocator.init();
    }
  }

  public RSACBeanLocator getRSACBeanLocator() {
    return rsacbeanlocator;
  }

}
