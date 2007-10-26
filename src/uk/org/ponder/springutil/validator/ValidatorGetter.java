/*
 * Created on 23 Oct 2007
 */
package uk.org.ponder.springutil.validator;

import org.springframework.validation.Validator;

/** Primitive getter interface returning a validator */

public interface ValidatorGetter {
  public Validator get();
}
