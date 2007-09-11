/*
 * Created on 7 Aug 2006
 */
package uk.org.ponder.springutil;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import uk.org.ponder.rsac.RSACBeanLocator;
import uk.org.ponder.stringutil.StringList;

/**
 * A {@link TargetListAggregatingBean} that will acquire all beans of a particular type
 * (excluding Factory products) and register either their names, or the beans
 * themselves, onto the TLAB target.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */

public class ByClassTLAB extends StaticTLAB implements ApplicationContextAware {
  private Class targetClass;
  private boolean deliverBeans = true;
  private RSACBeanLocator rsacbeanlocator;

  public void setTargetClass(Class targetClass) {
    this.targetClass = targetClass;
  }

  /**
   * If set to <code>false</code> (the default is <code>true</code>) this
   * will deliver a list the actual beans as the TLAB value rather than just a
   * list of their names.
   */
  public void setDeliverBeans(boolean deliverBeans) {
    this.deliverBeans = deliverBeans;
  }

  public void setRSACBeanLocator(RSACBeanLocator rsacbeanlocator) {
    this.rsacbeanlocator = rsacbeanlocator;
  }
  
  public void init() {
    if (rsacbeanlocator != null) {
      String[] beannames = rsacbeanlocator.beanNamesForClass(targetClass);
      applyNames(beannames);
    }
  }
  
  private void applyNames(String[] beannames) {
    if (deliverBeans) {
      setValueRefs(beannames);
    }
    else {
      setValue(new StringList(beannames));
    }
  }

  public void setApplicationContext(ApplicationContext applicationContext)
      throws BeansException {
    String[] beannames = applicationContext.getBeanNamesForType(targetClass,
        false, false);
    applyNames(beannames);
  }

}
