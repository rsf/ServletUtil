/*
 * Created on 7 Aug 2006
 */
package uk.org.ponder.springutil;

import java.util.ArrayList;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import uk.org.ponder.stringutil.StringList;

/**
 * A TargetListAggregatingBean that will acquire all beans of a particular type
 * (excluding Factory products) and register their *names* as the TLAB target.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */

public class ByClassTLAB extends StaticTLAB implements ApplicationContextAware {
  private Class targetClass;
  private boolean deliverBeans = true;

  public void setTargetClass(Class targetClass) {
    this.targetClass = targetClass;
  }

  /**
   * If set to <code>false</code> (the default is <code>true</code> this
   * will deliver a list the actual beans as the TLAB value rather than just a
   * list of their names.
   */
  public void setDeliverBeans(boolean deliverBeans) {
    this.deliverBeans = deliverBeans;
  }

  public void setApplicationContext(ApplicationContext applicationContext)
      throws BeansException {
    String[] beannames = applicationContext.getBeanNamesForType(targetClass,
        false, false);
    if (deliverBeans) {
      ArrayList beans = new ArrayList();
      for (int i = 0; i < beannames.length; ++i) {
        beans.add(applicationContext.getBean(beannames[i]));
      }
      setValue(beans);
    }
    else {
      setValue(new StringList(beannames));
    }
  }

}
