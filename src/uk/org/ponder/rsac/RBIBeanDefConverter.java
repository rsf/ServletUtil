/*
 * Created on 13 Nov 2006
 */
package uk.org.ponder.rsac;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class RBIBeanDefConverter implements BeanDefConverter {
  public List rbilist = new ArrayList();
  private ConfigurableListableBeanFactory clbf;

  public RBIBeanDefConverter(ConfigurableListableBeanFactory clbf) {
    this.clbf = clbf;
  }

  public void convertBeanDef(BeanDefinition origdef, String beanname,
      boolean inner) {
    RSACBeanInfo rbi = BeanDefUtil
        .convertBeanDef(origdef, beanname, clbf, this);
    rbi.beanname = beanname;
    if (inner) {
      // Follow Spring "spec" which says that inners are ALWAYS non-singletons.
      // This is in fact not reflected in its BeanDefinitions.
      rbi.issingleton = false;
    }
    rbilist.add(rbi);
  }

}
