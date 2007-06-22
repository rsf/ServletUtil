/*
 * Created on Dec 25, 2006
 */
package uk.org.ponder.rsac.test;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import uk.org.ponder.rsac.RSACResourceLocator;

public class LocalRSACResourceLocator implements ApplicationContextAware, RSACResourceLocator {
  private ApplicationContext applicationContext;
  private String[] configLocations;

  public void setConfigLocations(String[] configLocations) {
    this.configLocations = configLocations;
  }

  public ApplicationContext getApplicationContext() {
    return applicationContext;
  }

  public String[] getConfigLocations() {
    return configLocations;
  }

  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

}
