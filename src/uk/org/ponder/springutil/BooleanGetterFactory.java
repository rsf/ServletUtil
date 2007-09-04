/*
 * Created on 4 Sep 2007
 */
package uk.org.ponder.springutil;

import uk.org.ponder.booleanutil.BooleanGetter;
import uk.org.ponder.booleanutil.BooleanHolder;

public class BooleanGetterFactory {
  
    private Boolean value;

    public void setValue(Boolean value) {
      this.value = value;
    }
    
    public Object getObject() throws Exception {
      return new BooleanHolder(value);
    }

    public Class getObjectType() {
      return BooleanGetter.class;
    }

    public boolean isSingleton() {
      return true;
    }



}
