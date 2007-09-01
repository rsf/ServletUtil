/*
 * Created on May 17, 2006
 */
package uk.org.ponder.rsac;

import uk.org.ponder.stringutil.StringGetter;

/** Bridges a String value from request scope to application scope **/

public class RSACStringBridge implements StringGetter {

  private RSACBeanLocatorImpl rsacbl;
  private String targetbean;

  public void setRSACBeanLocator(RSACBeanLocatorImpl rsacbl) {
    this.rsacbl = rsacbl;
  }

  public void setTargetBeanName(String targetbean) {
    this.targetbean = targetbean;
  }
  
  public String get() {
    String value = (String) rsacbl.getBeanLocator().locateBean(targetbean);
    return value;
  }

}
