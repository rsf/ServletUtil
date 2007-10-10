/*
 * Created on 27-Feb-2006
 */
package uk.org.ponder.rsac;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;

/**
 * An "AOP Alliance" proxy that allows request-scope dependencies to be injected
 * into application scope. A parent bean definition is in
 * <code>rsf-config.xml</code> - just inherit from it, and set the
 * <code>targetBeanName</code> field, preferably using an <code>idref</code>
 * for link-safety. For example:
 * 
 * <pre>
 *    &lt;bean id=&quot;RSACSafeBeanLocatorProxy&quot; parent=&quot;RSACBridgeProxy&quot;&gt;
 *      &lt;property name=&quot;targetBeanName&quot;&gt;
 *        &lt;idref bean=&quot;rsacSafeBeanLocator&quot; /&gt;
 *      &lt;/property&gt;
 *    &lt;/bean&gt;
 * </pre>
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */

public class RSACBridgeProxy implements TargetSource, FactoryBean,
    BeanFactoryAware {

  private RSACBeanLocator rsacbl;
  private String targetbean;
  private boolean pea = false;
  private Class targetclass;

  public void setRSACBeanLocator(RSACBeanLocator rsacbl) {
    this.rsacbl = rsacbl;
  }

  public void setTargetBeanName(String targetbean) {
    this.targetbean = targetbean;
  }

  public Class getTargetClass() {
    return targetclass == null? rsacbl.getBeanClass(targetbean) : targetclass;
  }

  public void setTargetClass(Class targetclass) {
    this.targetclass = targetclass;
  }
  
  public boolean isStatic() {
    return false;
  }

  public void setPea(boolean pea) {
    this.pea = pea;
  }

  public Object getTarget() throws Exception {
    return rsacbl.getBeanLocator().locateBean(targetbean);
  }

  public void releaseTarget(Object target) throws Exception {
    // apparently no action required here.
  }

  Object proxy = null;
  private BeanFactory beanFactory;
  Object peaproxy = null;

  private void createProxy() {
    Class beanclass = getTargetClass();
    if (pea) {
      RSACPeaProxyFactory ppf = new RSACPeaProxyFactory(beanclass, this);
      proxy = ppf.getProxy();
    }
    else {
      ProxyFactoryBean pfb = new ProxyFactoryBean();
   
      if (beanclass.isInterface()) {
        pfb.setInterfaces(new Class[] { beanclass });
      }
      else {
        pfb.setProxyTargetClass(true);
      }
      pfb.setTargetSource(this);
      pfb.setBeanFactory(beanFactory);
      
      proxy = pfb.getObject();
    }
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

  public void setBeanFactory(BeanFactory beanFactory) {
    this.beanFactory = beanFactory;
  }
}
