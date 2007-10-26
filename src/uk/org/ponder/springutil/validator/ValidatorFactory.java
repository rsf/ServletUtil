/*
 * Created on 12 Sep 2007
 */
package uk.org.ponder.springutil.validator;

/** Root class of a Validator which is capable of being parsed from a 
 * compact String representation. If it does not implement any further
 * mixins, it is assumed to be a Spring validator requiring no argument.
 * That is, a validator which is either present or absent on a field.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public interface ValidatorFactory {
  /** Returns the name of the dispensed validator **/
  public String getName();

  /** Returns the priority of the validator. If there are multiple potential
   * validation failures, only those with the lowest priority number
   * will be reported.
   */
  public int getPriority();
  
  /** Returns an instance of the validator, of one of the types recognised
   * by the system.
   */
  public Object getValidator();
  
}
