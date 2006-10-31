/*
 * Created on May 29, 2006
 */
package uk.org.ponder.rsac;

import java.util.Locale;

import uk.org.ponder.localeutil.LocaleGetter;


/** Yet another final, interfaceless class in the JDK! This class bridges
 * Locales from request-scope. **/

public class RSACLocaleBridge implements LocaleGetter {

  private RSACBeanLocator rsacbl;
  private String targetbean;

  public void setRSACBeanLocator(RSACBeanLocator rsacbl) {
    this.rsacbl = rsacbl;
  }

  public void setTargetBeanName(String targetbean) {
    this.targetbean = targetbean;
  }
  
  public Locale get() {
    Locale value = (Locale) rsacbl.getBeanLocator().locateBean(targetbean);
    return value;
  }
}

