/*
 * Created on 12 Sep 2007
 */
package uk.org.ponder.springutil.validator.support;

import org.springframework.validation.Errors;

import uk.org.ponder.springutil.validator.ValidatorFactory;

public class RequiredValidator implements ValidatorFactory {
  public String getName() {
    return "required";
  }

  public Object getValidator() {
    return new ValidatorBase() {
      public void validate(Object obj, Errors errors) {
        if (obj == null || (obj instanceof String && (((String)obj).length() == 0))) 
          reject(errors, getName());
      }
    };
  }

  public int getPriority() {
    return 0;
  }

}
