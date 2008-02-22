/*
 * Created on 13 Nov 2006
 */
package uk.org.ponder.rsac.support;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;

import uk.org.ponder.rsac.BeanDefConverter;
import uk.org.ponder.saxalizer.SAXalizerMappingContext;
import uk.org.ponder.saxalizer.support.MethodAnalyser;

public class RBIBeanDefConverter implements BeanDefConverter {
  public List rbilist = new ArrayList();
  private ConfigurableListableBeanFactory clbf;
  private MethodAnalyser abdanalyser;

  public RBIBeanDefConverter(ConfigurableListableBeanFactory clbf, 
      SAXalizerMappingContext smc) {
    this.clbf = clbf;
    this.abdanalyser = smc.getAnalyser(AbstractBeanDefinition.class);
  }

  public void convertBeanDef(BeanDefinition origdef, String beanname,
      boolean inner) {
    RSACBeanInfo rbi = BeanDefUtil
        .convertBeanDef(origdef, beanname, clbf, abdanalyser, this);
    rbi.beanname = beanname;
    if (inner) {
      // Follow Spring "spec" which says that inners are ALWAYS non-singletons.
      // This is in fact not reflected in its BeanDefinitions.
      rbi.issingleton = false;
    }
    rbilist.add(rbi);
  }

}
