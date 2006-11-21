/*
 * Created on 16 Nov 2006
 */
package uk.org.ponder.rsac;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import uk.org.ponder.beanutil.BeanUtil;
import uk.org.ponder.beanutil.WriteableBeanLocator;
import uk.org.ponder.util.Logger;
import uk.org.ponder.util.UniversalRuntimeException;

/**
 * A list of Runnables to be invoked on (that is, immediately *after*) RSAC
 * context destruction. These are not registered as Spring standard listeners
 * (beans) because they are intended to execute once their parent context is
 * destroyed - all relevant configuration from RSAC must have been closed over.
 * 
 * An RSACLazarus element may perform any "exterior" action, including (usually)
 * firing another RSAC.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class RSACLazarusList implements Runnable {
  /** The standard bean name within RSAC of this instance */
  public static final String RSAC_LAZARUS_LIST = "RSACLazarusList";

  private List lazarusList = new ArrayList();

  private RSACBeanLocator rsacbl;

  public void queueRunnable(Runnable torun) {
    lazarusList.add(torun);
  }

  public void setRSACBeanLocator(RSACBeanLocator rsacbl) {
    this.rsacbl = rsacbl;
  }

  /** Invoked in the context of an *existing* RSAC request, returning
   * a Runnable which may be invoked as a Lazarus element which will 
   * start a further one.
   * @param newbeans a Map of any new beans to populate the initial,
   * subsequent RSAC container (overriding any which have been inherited as
   * "seeds" from the current invocation)
   * @param rootbeanname the name of the "root bean" to be fetched to trigger
   * the subsequent RSAC computation.
   */
  public Runnable getLazarusRunnable(final Map newbeans,
      final String rootbeanname) {
    return new Runnable() {
      Map seedbeans = rsacbl.getSeedMap();

      public void run() {
        rsacbl.startRequest();
        try {
          WriteableBeanLocator wbl = rsacbl.getBeanLocator();
          BeanUtil.copyBeans(seedbeans, wbl);
          BeanUtil.copyBeans(newbeans, wbl);
          Object fetch = wbl.locateBean(rootbeanname);
        }
        catch (Exception e) {
          throw UniversalRuntimeException.accumulate(e,
              "Error servicing RSACLazarus request");
        }
        finally {
          rsacbl.endRequest();
        }
      }
    };
  }

  public void run() {
    for (int i = 0; i < lazarusList.size(); ++i) {
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
