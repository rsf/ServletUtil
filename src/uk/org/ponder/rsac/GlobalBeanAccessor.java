/*
 * Created on 07-May-2006
 */
package uk.org.ponder.rsac;

import org.springframework.context.ApplicationContext;

/** Provides a global point of access to application-scope beans, bound to the
 * current thread. This is for use <b>only in extreme emergencies</b> - it
 * represents a terrible architectural risk. (In RSF it is used to allow
 * ViewParameters, which are tiny, uniquitous objects, to clone themselves).
 * Bean names should never ordinarily occur in Java code! 
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */ 

public class GlobalBeanAccessor {
  private static ThreadLocal contexts = new ThreadLocal();
  /** Returns the (application-scope) bean with the given name, bound to
   * the context for the current thread. Only use this method in a genuine
   * emergency!
   * @param name
   * @return
   */
  public static Object getBean(String name) {
    return ((ApplicationContext)contexts.get()).getBean(name);
  }
  public static void startRequest(ApplicationContext context) {
    contexts.set(context);
  }
  
  public static void endRequest() {
    contexts.set(null);
  }
}
