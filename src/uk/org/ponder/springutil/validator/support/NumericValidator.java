/*
 * Created on 12 Sep 2007
 */
package uk.org.ponder.springutil.validator.support;

import java.math.BigDecimal;

import org.springframework.validation.Errors;

import uk.org.ponder.springutil.validator.ValidatorFactory;

public class NumericValidator implements ValidatorFactory {
  public String getName() {
    return "numeric";
  }
  
  public Object getValidator() {
    return new ValidatorBase() {

      public void validate(Object obj, Errors errors) {
        String strobj = obj.toString();
        try {
          BigDecimal bd = new BigDecimal(strobj);
          if (!validNumber(bd)) {
            reject(errors, getName());
          }
        }
        catch (Exception e) {
          reject(errors, getName());
        }
      }
      
    };
  }

  public boolean validNumber(BigDecimal number) {
    return true;
  }

  public int getPriority() {
    return 1;
  }
  
}
