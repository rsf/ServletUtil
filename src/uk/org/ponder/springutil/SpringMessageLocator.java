/*
 * Created on Nov 23, 2004
 */
package uk.org.ponder.springutil;

import java.util.Locale;

import org.springframework.context.MessageSource;

import uk.org.ponder.errorutil.MessageLocator;
import uk.org.ponder.stringutil.LocaleGetter;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public class SpringMessageLocator extends MessageLocator {
  private MessageSource messagesource;
  private LocaleGetter localegetter;

  public void setMessageSource(MessageSource messagesource) {
    this.messagesource = messagesource;
  }

  public void setLocaleGetter(LocaleGetter localegetter) {
    this.localegetter = localegetter;
  }

  public String getMessage(String code, Object[] args) {
    return messagesource.getMessage(code, args, localegetter == null ? Locale
        .getDefault()
        : localegetter.get());
  }
}
