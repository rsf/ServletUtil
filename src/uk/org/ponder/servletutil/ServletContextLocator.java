/*
 * Created on 16 Mar 2007
 */
package uk.org.ponder.servletutil;

import javax.servlet.ServletContext;

public interface ServletContextLocator {
  /** Looks up a context registered under the supplied name, returning
   * <code>null</code> if no context is registered.
   */
  public ServletContext locateContext(String contextName);
}
