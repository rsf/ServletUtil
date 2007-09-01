/*
 * Created on 5 Aug 2006
 */
package uk.org.ponder.rsac;

import uk.org.ponder.errorutil.ErrorStateEntry;
import uk.org.ponder.errorutil.ThreadErrorState;
import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.util.RunnableInvoker;

/**
 * Link the PonderUtilCore ThreadErrorState marker with the lifecycle of a
 * request bean (in practice as a BeanFetchBracketer)
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class RSACErrorBridge implements RunnableInvoker {

  private String tmlbeanname;
  private RSACBeanLocatorImpl rsacbeanlocator;

  public void setRSACBeanLocator(RSACBeanLocatorImpl rsacbeanlocator) {
    this.rsacbeanlocator = rsacbeanlocator;
  }

  public void setTMLBeanName(String tmlbeanname) {
    this.tmlbeanname = tmlbeanname;
  }

  public void invokeRunnable(Runnable towrap) {
    TargettedMessageList tml = (TargettedMessageList) rsacbeanlocator
        .getBeanLocator().locateBean(tmlbeanname);
    ThreadErrorState.beginRequest();
    ErrorStateEntry ese = ThreadErrorState.getErrorState();
    ese.messages = tml;
    try {
      towrap.run();
    }
    finally {
      ThreadErrorState.endRequest();
    }
  }

}
