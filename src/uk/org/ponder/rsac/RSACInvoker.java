/*
 * Created on 10 Oct 2007
 */
package uk.org.ponder.rsac;

import org.springframework.context.ApplicationContext;

import uk.org.ponder.beanutil.WBLAcceptor;

public class RSACInvoker {
  public static Object doInRSAC(ApplicationContext applicationContext, WBLAcceptor acceptor) {
    RSACBeanLocator rsacbl = (RSACBeanLocator) applicationContext
    .getBean(RSACBeanLocator.RSAC_BEAN_LOCATOR_NAME);
    
    rsacbl.startRequest();
    try {
      return acceptor.acceptWBL(rsacbl.getBeanLocator());
    }
    finally {
      rsacbl.endRequest();
    }
  }
}
