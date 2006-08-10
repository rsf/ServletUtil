/*
 * Created on 5 Aug 2006
 */
package uk.org.ponder.rsac;

import uk.org.ponder.errorutil.ErrorStateEntry;
import uk.org.ponder.errorutil.TargettedMessageList;
import uk.org.ponder.errorutil.ThreadErrorState;
import uk.org.ponder.util.RunnableWrapper;

/** Link the PonderUtilCore ThreadErrorState marker with the lifecycle
 * of a request bean (in practice as a BeanFetchBracketer)
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class RSACErrorBridge implements RunnableWrapper {

  private String tmlbeanname;
  private RSACBeanLocator rsacbeanlocator;

  public void setRSACBeanLocator(RSACBeanLocator rsacbeanlocator) {
    this.rsacbeanlocator = rsacbeanlocator;
  }

  public void setTMLBeanName(String tmlbeanname) {
    this.tmlbeanname = tmlbeanname;
  }
  
  public Runnable wrapRunnable(final Runnable towrap) {
    return new Runnable() {
      public void run() {
        TargettedMessageList tml = 
          (TargettedMessageList) rsacbeanlocator.getBeanLocator().locateBean(tmlbeanname);
        ThreadErrorState.beginRequest();
        ErrorStateEntry ese = ThreadErrorState.getErrorState();
        ese.errors = tml;
        try {
          towrap.run();
        }
        finally {
          ThreadErrorState.endRequest();
        }
      }
    };
  }

}
