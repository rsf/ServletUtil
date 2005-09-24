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
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.ServletContextResourceLoader;
import org.springframework.web.context.support.ServletContextResourcePatternResolver;

import uk.org.ponder.util.UniversalRuntimeException;

/**
 * This code, grievously stolen from Andrew Thornton, hides away all the
 * unpleasant munging required to obtain a "Generic" Spring application context
 * using standard Spring resource location semantics.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */

public class RSACBeanFactoryFactory implements ApplicationContextAware, ApplicationListener {
  private RSACBeanGetter rsacbeangetter = null;

  /**
   * Creates a RequestScopeAppContextPool from the given config locations and
   * parent Application Context
   * 
   * @param configLocation
   * @param parent
   * @return a new pool
   */
  public static RSACBeanGetter createFactory(String configLocation,
      ApplicationContext parent, ServletContext context) {
    GenericApplicationContext initialContext = new GenericApplicationContext();
    XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(
        initialContext);

    Resource[] resources;
    try {
      resources = new ServletContextResourcePatternResolver(
          new ServletContextResourceLoader(context))
          .getResources(configLocation);
    }
    catch (IOException e) {
      throw UniversalRuntimeException.accumulate(e, "ConfigLocation: "
          + configLocation + " causes cannot be loaded ");
    }

    beanDefinitionReader.loadBeanDefinitions(resources);

    initialContext.setParent(parent);

    return new RSACBeanGetter(initialContext);

  }

  public void setApplicationContext(ApplicationContext applicationContext)
      throws BeansException {
    WebApplicationContext wac = (WebApplicationContext) applicationContext;
    ServletContext sc = wac.getServletContext();
    String location = sc.getInitParameter("requestContextConfigLocation");
    rsacbeangetter = createFactory(location, wac, sc);
  }


  public void onApplicationEvent(ApplicationEvent event) {
    if (event instanceof ContextRefreshedEvent) {
      rsacbeangetter.init();
    }
  }
  
  public RSACBeanGetter getRSACBeanGetter() {
    return rsacbeangetter;
  }

}
