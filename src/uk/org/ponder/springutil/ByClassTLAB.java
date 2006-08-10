/*
 * Created on 7 Aug 2006
 */
package uk.org.ponder.springutil;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import uk.org.ponder.stringutil.StringList;

/** A TargetListAggregatingBean that will acquire all beans of a particular
 * type (excluding Factory products) and automatically make them 
 *  request-addressible.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class ByClassTLAB extends StaticTLAB implements ApplicationContextAware {
  private Class targetClass;

  public void setTargetClass(Class targetClass) {
    this.targetClass = targetClass;
  }
  
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    String[] beannames = applicationContext.getBeanNamesForType(
        targetClass, false, false);
    setValue(new StringList(beannames));
  }

}
