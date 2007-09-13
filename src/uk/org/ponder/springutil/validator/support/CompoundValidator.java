/*
 * Created on 12 Sep 2007
 */
package uk.org.ponder.springutil.validator.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class CompoundValidator implements Validator {
  private List validators = new ArrayList();

  public boolean supports(Class clazz) {
    return true;
  }

  public void validate(Object obj, Errors errors) {
    for (int i = 0; i < validators.size(); ++ i) {
      ((Validator)validators.get(i)).validate(obj, errors);
    }
  }
  
  public void addValidator(Validator validator) {
    validators.add(validator);
  }
  
  public CompoundValidator(Validator validator1, Validator validator2) {
    addValidator(validator1);
    addValidator(validator2);
  }

  public CompoundValidator(List validators) {
    this.validators.addAll(validators);
  }

  public CompoundValidator() {
  }
}
