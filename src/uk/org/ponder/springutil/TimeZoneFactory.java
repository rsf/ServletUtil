/*
 * Created on 30-Nov-2006
 */
package uk.org.ponder.springutil;

import java.util.TimeZone;

import org.springframework.beans.factory.FactoryBean;

public class TimeZoneFactory implements FactoryBean {
  public Object getObject() throws Exception {
    return TimeZone.getDefault();
  }

  public Class getObjectType() {
    return TimeZone.class;
  }

  public boolean isSingleton() {
    return true;
  }

}
