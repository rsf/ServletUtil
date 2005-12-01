/*
 * Created on Dec 1, 2005
 */
package uk.org.ponder.springutil;

import java.util.List;

import org.springframework.beans.factory.FactoryBean;

import uk.org.ponder.stringutil.StringList;

public class StringListAggregator implements FactoryBean {
  private StringList stringlist;

  public Object getObject() throws Exception {
    return stringlist;
  }

  public Class getObjectType() {
    return StringList.class;
  }

  public boolean isSingleton() {
    return true;
  }

  public void setStringLists(List stringlists) {
    stringlist = new StringList();
    for (int i = 0; i < stringlists.size(); ++i) {
      List listat = (List) stringlists.get(i);
      stringlist.addAll(listat);
    }
  }
}
