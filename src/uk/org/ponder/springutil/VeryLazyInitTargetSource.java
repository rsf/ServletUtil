/*
 * Created on Mar 30, 2006
 */
package uk.org.ponder.springutil;

import org.springframework.aop.TargetSource;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;

/** A Spring TargetSource that is "even more lazy" than LazyInitTargetSource,
 * in that it does not assume that the target bean reference is resolvable
 * at the time of creation. This is necessary to support "unreasonable" models
 * where bean definitions are being dynamically loaded, or parent containers
 * attached.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class VeryLazyInitTargetSource implements TargetSource, BeanFactoryAware {
  private Class targetclass;
  private BeanFactory beanFactory;
  private String targetBeanName;

  public Class getTargetClass() {
    return targetclass;
  }

  public void setTargetClass(Class targetclass) {
    this.targetclass = targetclass;
  }
  
  public boolean isStatic() {
    return false;
  }

  public Object getTarget() throws Exception {
    return beanFactory.getBean(targetBeanName);
  }

  public void releaseTarget(Object target) throws Exception {
   
  }

  public void setTargetBeanName(String targetBeanName) {
    this.targetBeanName = targetBeanName;
  }
  
  public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
    this.beanFactory = beanFactory;
  }

}
