/*
 * Created on 10-Mar-2006
 */
package uk.org.ponder.rsac;

/** Used as a marker through createBean to track progress through fetch
 * wrappers.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

class CreationMarker {
  public CreationMarker(int i) {
    wrapperindex = i;
  }

  public int wrapperindex = 0;
}
