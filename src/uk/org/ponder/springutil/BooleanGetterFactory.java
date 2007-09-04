/*
 * Created on 4 Sep 2007
 */
package uk.org.ponder.springutil;

import org.springframework.beans.factory.FactoryBean;

import uk.org.ponder.booleanutil.BooleanGetter;
import uk.org.ponder.booleanutil.BooleanHolder;

public class BooleanGetterFactory implements FactoryBean {
  
    private Boolean value;

    public void setValue(Boolean value) {
      this.value = value;
    }
    
    public Object getObject() throws Exception {
      return new BooleanHolder(value.booleanValue());
    }

    public Class getObjectType() {
      return BooleanGetter.class;
    }

    public boolean isSingleton() {
      return true;
    }



}
