/*
 * Created on Jun 15, 2005
 */
package uk.org.ponder.springutil;

import org.springframework.beans.factory.BeanNameAware;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
public interface URLAwareController extends BeanNameAware {
  public abstract String getBeanName();
  public void setReverseURLMapper(ReverseURLMapper mapper);
}
