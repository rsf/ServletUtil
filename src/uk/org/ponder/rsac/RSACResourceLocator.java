/*
 * Created on Nov 19, 2005
 */
package uk.org.ponder.rsac;

import org.springframework.context.ApplicationContext;

/** All the information required (apart from the parent ApplicationContext)
 * for the RSACBeanLocator to be initialised.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public interface RSACResourceLocator {
  public String[] getConfigLocation();
  public ApplicationContext getApplicationContext();
}
