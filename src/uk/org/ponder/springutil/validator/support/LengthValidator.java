/*
 * Created on 12 Sep 2007
 */
package uk.org.ponder.springutil.validator.support;

import org.springframework.validation.Errors;

import uk.org.ponder.springutil.validator.ValidatorArgReporter;
import uk.org.ponder.springutil.validator.ValidatorFactory;

public abstract class LengthValidator implements ValidatorFactory, ValidatorArgReporter {
  public class Validator extends ValidatorBase {
    private int length;
    public void validate(Object obj, Errors errors) {
      if (obj instanceof String && !validateLength((String)obj ,length)) {
        reject(errors, getName());
      }
    }
    public void setLength(int length) {
      this.length = length;
    }
  }
  public Object getValidator() {
    return new Validator() {
    };
  }

  public abstract boolean validateLength(String val, int length);
  
  public String getValidatorArgMethods() {
   return "length";
  }
  
  public int getPriority() {
    return 1;
  }
}
