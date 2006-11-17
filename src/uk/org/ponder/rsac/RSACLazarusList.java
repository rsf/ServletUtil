/*
 * Created on 16 Nov 2006
 */
package uk.org.ponder.rsac;

import java.util.ArrayList;
import java.util.List;

import uk.org.ponder.util.Logger;
import uk.org.ponder.util.RunnableInvoker;

/** A list of Runnables to be invoked on (that is, immediately *after*) RSAC
 * context destruction. These are not registered as Spring standard listeners
 * (beans) because they are intended to execute once their parent context is
 * destroyed - all relevant configuration from RSAC must have been closed over.
 * 
 * An RSACLazarus element may perform any "exterior" action, 
 * including (usually) firing another RSAC.
 *  
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class RSACLazarusList implements RunnableInvoker, Runnable {
  /** The standard bean name within RSAC of this instance */
  public static final String RSAC_LAZARUS_LIST = "RSACLazarusList";
  
  private List lazarusList = new ArrayList();
  public void invokeRunnable(Runnable torun) {
    lazarusList.add(torun);
  }
  
  public void run() {
    for (int i = 0; i < lazarusList.size(); ++ i) {
      Runnable torun = (Runnable) lazarusList.get(i);
      try {
       torun.run();
      }
      catch (Exception e) {
        Logger.log.warn("Error invoking lazarus action: ", e);
      }
    }
  }

}
