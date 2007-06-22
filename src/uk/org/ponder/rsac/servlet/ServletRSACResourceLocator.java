/*
 * Created on Nov 19, 2005
 */
package uk.org.ponder.rsac.servlet;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.StringUtils;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import uk.org.ponder.rsac.RSACResourceLocator;

/** Decodes the locations of request-scope bean containers from the 
 * parent ServletContext of the current application context.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class ServletRSACResourceLocator implements RSACResourceLocator, 
    ApplicationContextAware {
  public static final String REQUEST_CONTEXT_CONFIG_LOCATION = "requestContextConfigLocation";
  
  private String[] configlocations;
  private ApplicationContext applicationcontext;
  public void setApplicationContext(ApplicationContext applicationContext)
      throws BeansException {
    this.applicationcontext = applicationContext;
    WebApplicationContext wac = (WebApplicationContext) applicationContext;
    ServletContext context = wac.getServletContext();
    String configlocation = context.getInitParameter(REQUEST_CONTEXT_CONFIG_LOCATION);
    configlocations = StringUtils.tokenizeToStringArray(configlocation,
        ConfigurableWebApplicationContext.CONFIG_LOCATION_DELIMITERS);
  }

  public String[] getConfigLocations() {
    return configlocations;
  }

  public ApplicationContext getApplicationContext() {
    return applicationcontext;
  }

}
