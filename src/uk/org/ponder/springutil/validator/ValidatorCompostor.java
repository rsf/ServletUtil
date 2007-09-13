/*
 * Created on 13 Sep 2007
 */
package uk.org.ponder.springutil.validator;

import org.springframework.validation.Validator;

/** Parses validators out of a primitive String specification into a
 * concrete implementation (currently) as a Spring validator.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public interface ValidatorCompostor {

  public Validator parseValidator(String spec);

}