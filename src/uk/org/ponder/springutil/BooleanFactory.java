/*
 * Created on 7 Aug 2006
 */
package uk.org.ponder.springutil;

import org.springframework.beans.factory.FactoryBean;

// hopefully temporary class, getting around the fact that RSAC does not
// support constructor args
public class BooleanFactory implements FactoryBean {
  private Boolean value;

  public void setValue(Boolean value) {
    this.value = value;
  }
  
  public Object getObject() throws Exception {
    return value;
  }

  public Class getObjectType() {
    return Boolean.class;
  }

  public boolean isSingleton() {
    return true;
  }

}
