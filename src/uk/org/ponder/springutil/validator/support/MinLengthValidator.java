/*
 * Created on 12 Sep 2007
 */
package uk.org.ponder.springutil.validator.support;

public class MinLengthValidator extends LengthValidator {
  public String getName() {
    return "minLength";
  }
  public boolean validateLength(String val, int length) {
    return val.length() >= length;
  }

}
