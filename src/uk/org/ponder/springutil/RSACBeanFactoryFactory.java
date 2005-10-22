/*
 * Created on Sep 18, 2005
 */
package uk.org.ponder.springutil;

import java.io.IOException;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.ServletContextResourceLoader;
import org.springframework.web.context.support.ServletContextResourcePatternResolver;

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

public class RSACBeanFactoryFactory implements ApplicationContextAware,
    ApplicationListener {
  private RSACBeanLocator rsacbeanlocator = null;

  /**
   * Creates a RequestScopeAppContextPool from the given config locations and
   * parent Application Context
   * 
   * @param configLocation
   * @param parent
   * @return a new pool
   */
  public static RSACBeanLocator createFactory(String configLocation,
      ApplicationContext parent, ServletContext context) {
    GenericApplicationContext initialContext = new GenericApplicationContext();
    XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(
        initialContext);

    Resource[] resources = new Resource[0];
    try {
      if (configLocation != null) {
        ServletContextResourceLoader loader = new ServletContextResourceLoader(context);
        ServletContextResourcePatternResolver resolver = new ServletContextResourcePatternResolver(
            loader);
        String[] locations = StringUtils.tokenizeToStringArray(configLocation,
            ConfigurableWebApplicationContext.CONFIG_LOCATION_DELIMITERS);
        for (int i = 0; i < locations.length; ++i) {
          Resource[] newresources = resolver.getResources(locations[i]);
          resources = (Resource[]) ArrayUtil.concat(resources, newresources);
        }
      }

    }
    catch (IOException e) {
      throw UniversalRuntimeException.accumulate(e, "ConfigLocation: "
          + configLocation + " causes cannot be loaded ");
    }

    beanDefinitionReader.loadBeanDefinitions(resources);

    initialContext.setParent(parent);

    return new RSACBeanLocator(initialContext);

  }

  public void setApplicationContext(ApplicationContext applicationContext)
      throws BeansException {
    WebApplicationContext wac = (WebApplicationContext) applicationContext;
    ServletContext sc = wac.getServletContext();
    String location = sc.getInitParameter("requestContextConfigLocation");
    rsacbeanlocator = createFactory(location, wac, sc);
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
