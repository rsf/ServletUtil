/*
 * Created on 25-Jan-2006
 */
package uk.org.ponder.rsac;

import org.springframework.beans.factory.InitializingBean;

import uk.org.ponder.beanutil.BeanLocator;

/** Since all RSAC beans are by default lazy, we requre this class to
 * force-load any beans that have their dependencies inverted for some
 * reason.
 * <p>Think of a better solution at some point.
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */

public class RSACBeanLoader implements InitializingBean {

  private BeanLocator beanlocator;
  private String[] beannames;

  public void setBeanLocator(BeanLocator beanlocator) {
    this.beanlocator = beanlocator;
  }
  
  public void setBeanNames(String[] beannames) {
    this.beannames = beannames;
  }

  public void afterPropertiesSet() {
    for (int i = 0; i < beannames.length; ++ i) {
      beanlocator.locateBean(beannames[i]);
    }
  }
}
