/*
 * Created on Nov 30, 2005
 */
package uk.org.ponder.springutil;

import org.springframework.beans.factory.FactoryBean;

import uk.org.ponder.stringutil.StringList;

public class StringListFactory implements FactoryBean {
  private StringList stringlist;
  private String delim = ",";
  public Object getObject() throws Exception {
    return stringlist;
  }

  public Class getObjectType() {
    return StringList.class;
  }

  public boolean isSingleton() {
    return true;
  }

  public void setDelimiter(String delim) {
    this.delim  = delim;
  }
  
  public void setStrings(String strings) {
    stringlist = StringList.fromString(strings);
  }
}
