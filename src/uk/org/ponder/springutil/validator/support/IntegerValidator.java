/*
 * Created on 12 Sep 2007
 */
package uk.org.ponder.springutil.validator.support;

import java.math.BigDecimal;

public class IntegerValidator extends NumericValidator {
  public String getName() {
    return "integer";
  }
  
  public boolean validNumber(BigDecimal bd) {
    return bd.scale() == 0;
  }
  
  public int getPriority() {
    return 2;
  }
}
