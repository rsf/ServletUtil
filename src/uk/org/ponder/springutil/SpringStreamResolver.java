/*
 * Created on 31-Dec-2005
 */
package uk.org.ponder.springutil;

import java.io.InputStream;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import uk.org.ponder.streamutil.StreamResolver;
import uk.org.ponder.util.UniversalRuntimeException;

/**
 * @author Antranig Basman (amb26@ponder.org.uk)
 * 
 */
public class SpringStreamResolver implements ApplicationContextAware,
    StreamResolver {
  private ResourceLoader resourceLoader;

  public SpringStreamResolver() {}

  public SpringStreamResolver(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }
  
  public void setApplicationContext(ApplicationContext applicationContext)
      throws BeansException {
    this.resourceLoader = applicationContext;
  }
  
  public InputStream openStream(String path) {
    try {
      Resource res = resourceLoader.getResource(path);
      return res.getInputStream();
    }
    catch (Exception e) {
      throw UniversalRuntimeException.accumulate(e,
          "Unable to open resource with path " + path
              + " from application context" + resourceLoader);
    }
  }

}
