/*
 * Created on May 17, 2006
 */
package uk.org.ponder.rsac;

import uk.org.ponder.stringutil.StringGetter;

/** Bridges a String value from request scope to application scope **/

public class RSACStringBridge implements StringGetter {

  private RSACBeanLocator rsacbl;
  private String targetbean;

  public void setRSACBeanLocator(RSACBeanLocator rsacbl) {
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
