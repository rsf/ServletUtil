/*
 * Created on 22 Oct 2007
 */
package uk.org.ponder.springutil.validator.support;

import org.springframework.validation.Validator;

import uk.org.ponder.springutil.validator.ValidatorCompostor;
import uk.org.ponder.springutil.validator.ValidatorGetter;
import uk.org.ponder.springutil.validator.ValidatorSpecHolder;

/**
 * Factory bean which automates parsing and collection of validator
 * specifications.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */

public class ValidatorBuilder implements ValidatorSpecHolder, ValidatorGetter {

  private ValidatorCompostor validatorCompostor;

  private String[] validatorSpecs;

  public void setValidatorCompostor(ValidatorCompostor validatorCompostor) {
    this.validatorCompostor = validatorCompostor;
  }

  public void setValidatorSpecs(String[] specs) {
    this.validatorSpecs = specs;
  }

  public String[] getValidatorSpecs() {
    return validatorSpecs;
  }

  public Object getObject() {
    return get();
  }

  public Class getObjectType() {
    return Validator.class;
  }

  public boolean isSingleton() {
    return true;
  }

  public Validator get() {
    CompoundValidator compoundValidator = new CompoundValidator();
    for (int i = 0; i < validatorSpecs.length; ++i) {
      compoundValidator.addValidator(validatorCompostor
          .parseValidator(validatorSpecs[i]));
    }
    return compoundValidator;
  }

}
