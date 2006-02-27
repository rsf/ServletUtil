/*
 * Created on 27-Feb-2006
 */
package uk.org.ponder.rsac;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.FactoryBean;

import uk.org.ponder.springutil.BeanLocatorBeanFactory;


/** An "AOP Alliance" proxy that allows request-scope dependencies to
 * be injected into application scope.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class RSACBridgeProxy implements TargetSource, FactoryBean {
  
  private RSACBeanLocator rsacbl;
  private String targetbean;

  public void setRSACBeanLocator(RSACBeanLocator rsacbl) {
    this.rsacbl = rsacbl;
  }
  
  public void setTargetBeanName(String targetbean) {
    this.targetbean = targetbean;
  }
  
  public Class getTargetClass() {
    return rsacbl.getBeanClass(targetbean);
  }

  public boolean isStatic() {
    return false;
  }

  public Object getTarget() throws Exception {
    return rsacbl.getBeanLocator().locateBean(targetbean);
  }

  public void releaseTarget(Object target) throws Exception {
    // apparently no action required here.
  }

  Object proxy = null;
  
  private void createProxy() {
    ProxyFactoryBean pfb = new ProxyFactoryBean();
    Class beanclass = getTargetClass();
    if (beanclass.isInterface()) {
      pfb.setInterfaces(new Class[] {beanclass});
    }
    pfb.setTargetSource(this);
    BeanFactory blfactory = new BeanLocatorBeanFactory(rsacbl.getBeanLocator());
    pfb.setBeanFactory(blfactory);
    proxy = pfb.getObject();
  }
  
  public Object getObject() throws Exception {
    if (proxy == null) {
      createProxy();
    }
    return proxy;
  }

  public Class getObjectType() {
    return getTargetClass();
  }

  public boolean isSingleton() {
    return true;
  }

}
