/*
 * Created on Nov 23, 2004
 */
package uk.org.ponder.springutil;

import java.util.Locale;

import org.springframework.context.MessageSource;

import uk.org.ponder.errorutil.MessageLocator;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class SpringMessageLocator extends MessageLocator {
  MessageSource messagesource;
  
  public void setMessageSource(MessageSource messagesource) {
    this.messagesource = messagesource;  
  }
  
  public String getMessage(String code, Object[] args) {
    // really should get user from threadmap, and find preferences.
    Locale locale = Locale.getDefault();
    return messagesource.getMessage(code, args, locale);
  }
}
