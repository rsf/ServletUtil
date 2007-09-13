/*
 * Created on 12 Sep 2007
 */
package uk.org.ponder.springutil.validator.support;

public class MaxLengthValidator extends LengthValidator {
  public String getName() {
    return "maxLength";
  }
  public boolean validateLength(String val, int length) {
    return val.length() <= length;
  }

}
