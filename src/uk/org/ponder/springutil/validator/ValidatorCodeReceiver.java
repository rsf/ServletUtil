/*
 * Created on 12 Sep 2007
 */
package uk.org.ponder.springutil.validator;

public interface ValidatorCodeReceiver {
  /** Sets the message code this validator will return in case of failure */
  public void setMessageCode(String code);
  
  public void setMessageArgs(Object[] args);
  
}
