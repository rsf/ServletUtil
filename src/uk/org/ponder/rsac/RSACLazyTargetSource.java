/*
 * Created on 17-Dec-2005
 */
package uk.org.ponder.rsac;

import org.springframework.aop.TargetSource;

/**
 * An RSAC-specific TargetSource, which will be permanently maintained in the
 * RSAC ThreadLocal container. A sort of multi-way cross between 
 * ThreadLocalTargetSource, HotSwappableTargetSource and
 * LazyInitTargetSource...
 * <p>An RLTS will be automatically created for every bean in the context
 * marked as <code>lazy-init="true"</code>, converting it into a 
 * <a href="http://www2.caret.cam.ac.uk/rsfwiki/Wiki.jsp?page=VeryLazyBeans">
 * very lazy bean</a>
 * <p>Note that the one exposed dependency, targetBeanName, is actually fake.
 * Since we have only ONE RLTS per thread, the name is actually stashed by
 * RSACBeanLocator, which on thread init creates not only a ProxyFactoryBean,
 * but also a forked instance of this bean, with all the dependencies 
 * delivered via constructor.
 * <p>This facility could be provided by a Spring AutoProxyCreator but there
 * isn't time during a request cycle.
 * @author Antranig Basman (amb26@ponder.org.uk)
 * 
 */
public class RSACLazyTargetSource implements TargetSource {
  private Class targetclass;
  private RSACBeanLocatorImpl rsacbl;
  private String targetbeanname;
  private PerRequestInfo pri;

  public RSACLazyTargetSource(RSACBeanLocatorImpl rsacbl,
      PerRequestInfo pri, 
      Class targetclass, String targetbeanname) {
    this.rsacbl = rsacbl;
    this.pri = pri;
    this.targetclass = targetclass;
    this.targetbeanname = targetbeanname;
  }
  
  public Class getTargetClass() {
    return targetclass;
  }

  public boolean isStatic() {
    return false;
  }

  public Object getTarget() {
    return rsacbl.getBean(pri, targetbeanname, true);
  }

  public void releaseTarget(Object target) throws Exception {
    // Apparently nothing is necessary to do here
  }

}
