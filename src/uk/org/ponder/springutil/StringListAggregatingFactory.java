/*
 * Created on Dec 1, 2005
 */
package uk.org.ponder.springutil;

import java.util.List;

import org.springframework.beans.factory.FactoryBean;

import uk.org.ponder.stringutil.StringList;

public class StringListAggregatingFactory implements FactoryBean {
  private StringList stringlist = new StringList();
  private String delim = ",";

  public Object getObject() {
    return stringlist;
  }

  public Class getObjectType() {
    return StringList.class;
  }

  public boolean isSingleton() {
    return true;
  }

  public void setDelimiter(String delim) {
    this.delim = delim;
  }

  public void setCollect(List collected) {
    for (int i = 0; i < collected.size(); ++i) {
      String toparse = (String) collected.get(i);
      StringList parsed = StringList.fromString(toparse);
      stringlist.addAll(parsed);
    }
  }
}
