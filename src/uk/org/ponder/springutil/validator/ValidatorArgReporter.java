/*
 * Created on 12 Sep 2007
 */
package uk.org.ponder.springutil.validator;

/** A validator which requires initialisation. The name of the initialisation
 * method is reported, which may have any scalar type.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public interface ValidatorArgReporter extends ValidatorFactory {
  /** Reports the name of a validator initialisation method held on 
   * this class. For example a method <code>set
   */
  public String getValidatorArgMethods();
}
