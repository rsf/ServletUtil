/*
 * Created on 12 Sep 2007
 */
package uk.org.ponder.springutil.validator.support;

import java.math.BigDecimal;

public class NegativeValidator extends NumericValidator {

  public String getName() {
    return "negative";
  }
  
  public boolean validNumber(BigDecimal bd) {
    return bd.compareTo(new BigDecimal(0)) < 0;
  }
  
  public int getPriority() {
    return 2;
  }
}
