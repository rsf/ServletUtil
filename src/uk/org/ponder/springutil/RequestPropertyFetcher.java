/*
 * Created on Oct 26, 2005
 */
package uk.org.ponder.springutil;

/** A convenient interface for clients who are already application beans,
 * and want the benefits of request-scope dependencies without the costs of
 * having to instantiate themselves for each request. The cost they pay is the
 * dependency on this framework class...
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class RequestPropertyFetcher {
  public static void fetchRequestDependence(Object target, String propertyname,
      String beanname) {
    
  }
}
