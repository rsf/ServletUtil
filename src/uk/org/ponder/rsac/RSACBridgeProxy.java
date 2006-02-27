/*
 * Created on 27-Feb-2006
 */
package uk.org.ponder.rsac;

import org.springframework.aop.TargetSource;


/** An "AOP Alliance" proxy that allows request-scope dependencies to
 * be injected into application scope.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class RSACBridgeProxy implements TargetSource {
  
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

}
