/*
 * Created on 1 Sep 2007
 */
package uk.org.ponder.rsac;

import uk.org.ponder.beanutil.WriteableBeanLocator;

/** A tag interface, marking a bean as not participating in the {@link RSACLazarusList}
 * resurrection scheme, despite being stored in the RSAC through "inchuck" (that is, 
 * through being written into it explicitly via its {@link WriteableBeanLocator} 
 * interface.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public interface UnLazarable {

}
