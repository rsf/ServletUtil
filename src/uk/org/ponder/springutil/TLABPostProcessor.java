/*
 * Created on 7 Aug 2006
 */
package uk.org.ponder.springutil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import uk.org.ponder.arrayutil.MapUtil;
import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.saxalizer.AccessMethod;
import uk.org.ponder.saxalizer.MethodAnalyser;
import uk.org.ponder.saxalizer.SAXalizerMappingContext;

/**
 * Does the work of collecting and focusing all the distributed property
 * deliveries onto the target list-valued bean property.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */

public class TLABPostProcessor implements BeanPostProcessor,
    ApplicationContextAware {

  private Map targetMap = new HashMap();

  private SAXalizerMappingContext mappingContext;
  private ApplicationContext applicationContext;
 // If this is set, will be used in preference to applicationContext to resolve beans
  private BeanLocator beanLocator;

  public void setMappingContext(SAXalizerMappingContext mappingContext) {
    this.mappingContext = mappingContext;
  }

  public void init() {
    if (mappingContext == null) {
      mappingContext = SAXalizerMappingContext.instance();
    }
  }
  
  // Intended for fast request-scope deployment
  public TLABPostProcessor copy() {
    TLABPostProcessor togo = new TLABPostProcessor();
    togo.targetMap = targetMap;
    togo.mappingContext = mappingContext;
    return togo;
  }
  
// VERY temporary method just to get support for bindbefore/bindafter="*"
  private void sortTLABs(List tlabs) {
    int limit = tlabs.size();
    for (int i = 0; i < limit; ++ i) {
      TargetListAggregatingBean tlab = (TargetListAggregatingBean) tlabs.get(i);
      Object bindafter = tlab.getBindAfter();
      if ("*".equals(bindafter)) {
        tlabs.remove(i);
        tlabs.add(tlab);
        --i; --limit;
      }
      Object bindbefore = tlab.getBindBefore();
      if ("*".equals(bindbefore)) {
        tlabs.remove(i);
        tlabs.add(0, tlab);
      }
    }
  }
  
  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
    // We do this here so that fewer will have to come after us!
    String[] viewbeans = applicationContext.getBeanNamesForType(
        TargetListAggregatingBean.class, false, false);
    for (int i = 0; i < viewbeans.length; ++i) {
      String viewbean = viewbeans[i];
      TargetListAggregatingBean tlab = (TargetListAggregatingBean) applicationContext
          .getBean(viewbean);
      if (!(tlab.getValue() == null ^ tlab.getValueRef() == null)) {
        throw new IllegalArgumentException("Error reading TargetListAggregatingBean " + viewbean + 
            ": exactly one of value or valueRef must be set");
      }
      MapUtil.putMultiMap(targetMap, tlab.getTargetBean(), tlab);
    }
    
    for (Iterator values = targetMap.values().iterator(); values.hasNext();) {
      List tlabs = (List) values.next();
      sortTLABs(tlabs);
    }
  }

  public void setBeanLocator(BeanLocator beanLocator) {
    this.beanLocator = beanLocator;
  }
  
  public Object postProcessAfterInitialization(Object bean, String beanName) {
    return bean;
  }

  public Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {
    // Perhaps in Ruby, Perl, or Haskell, this method body is just 4 lines!
    
    List tlabs = (List) targetMap.get(beanName);
    if (tlabs == null) return bean;
    Map listprops = new HashMap(); // map of property name to list
    for (int i = 0; i < tlabs.size(); ++i) {
      TargetListAggregatingBean tlab = (TargetListAggregatingBean) tlabs.get(i);
      Object value = tlab.getValue();
      if (value == null) {
        if (beanLocator != null) {
          value = beanLocator.locateBean(tlab.getValueRef());
        }
        else {
          value = applicationContext.getBean(tlab.getValueRef());
        }
      }
      if (tlab.getUnwrapLists() && value instanceof List) {
        List values = (List) value;
        for (int j = 0; j < values.size(); ++j) {
          MapUtil.putMultiMap(listprops, tlab.getTargetProperty(), values
              .get(j));
        }
      }
      else {
        MapUtil.putMultiMap(listprops, tlab.getTargetProperty(), value);
      }
    }
    for (Iterator propit = listprops.keySet().iterator(); propit.hasNext();) {
      String propname = (String) propit.next();
      Object value = listprops.get(propname);
      MethodAnalyser ma = mappingContext.getAnalyser(bean.getClass());
      AccessMethod sam = ma.getAccessMethod(propname);
      if (sam == null || !sam.canSet()) {
        throw new IllegalArgumentException("TLAB target bean " + beanName + " does not have any writeable property named " + propname);
      }
      sam.setChildObject(bean, value);
    }
    return bean;
  }

}
