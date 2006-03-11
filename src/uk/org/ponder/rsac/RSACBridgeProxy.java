/*
 * Created on 27-Feb-2006
 */
package uk.org.ponder.rsac;

import org.springframework.aop.TargetSource;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;


/** An "AOP Alliance" proxy that allows request-scope dependencies to
 * be injected into application scope. A parent bean definition is in 
 * <code>rsf-config.xml</code> - just inherit from it, and set the
 * <code>targetBeanName</code> field, preferably using an <code>idref</code>
 * for link-safety. For example:
 * <pre>
 * &lt;bean id="RSACSafeBeanLocatorProxy" parent="RSACBridgeProxy"&gt;
 *   &lt;property name="targetBeanName"&gt;
 *     &lt;idref bean="rsacSafeBeanLocator" /&gt;
 *   &lt;/property&gt;
 * &lt;/bean&gt;
 * </pre>
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class RSACBridgeProxy implements TargetSource, FactoryBean,
  BeanFactoryAware {
  
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
  private BeanFactory beanFactory;
  
  private void createProxy() {
    ProxyFactoryBean pfb = new ProxyFactoryBean();
    Class beanclass = getTargetClass();
    if (beanclass.isInterface()) {
      pfb.setInterfaces(new Class[] {beanclass});
    }
    pfb.setTargetSource(this);
    pfb.setBeanFactory(beanFactory);
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

  public void setBeanFactory(BeanFactory beanFactory)  {
    this.beanFactory = beanFactory;
  }

}
