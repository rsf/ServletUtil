/*
 * Created on 12 Sep 2007
 */
package uk.org.ponder.springutil.validator.support;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import uk.org.ponder.springutil.validator.ValidatorCodeReceiver;

/** A base class abstracting commonly used functionality from primitive 
 * validator instances, allowing their individual function to be parameterised
 * solely from the validate() method required 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public abstract class ValidatorBase implements Validator, ValidatorCodeReceiver {
  protected String messageCode;
  private String offsetPath;
  private int priority;
  private Object[] messageArgs;

  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }

  public String getOffsetPath() {
    return offsetPath;
  }

  public void setOffsetPath(String offsetPath) {
    this.offsetPath = offsetPath;
  }

  public void setMessageCode(String code) {
    this.messageCode = code;
  }

  public void setMessageArgs(Object[] args) {
    this.messageArgs = args;
  }
  
  public boolean supports(Class clazz) {
    return true;
  }
  
  public void reject(Errors errors, String defaultCode) {
    String code = messageCode == null? defaultCode: messageCode;
    if (messageArgs == null) {
      errors.rejectValue(null, code);
    }
    else {
      errors.rejectValue(null, code, messageArgs, null);
    }
  }

}
