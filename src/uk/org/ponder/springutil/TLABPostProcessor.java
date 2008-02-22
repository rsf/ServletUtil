/*
 * Created on 7 Aug 2006
 */
package uk.org.ponder.springutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import uk.org.ponder.arrayutil.MapUtil;
import uk.org.ponder.beanutil.BeanLocator;
import uk.org.ponder.rsac.RSACBeanLocator;
import uk.org.ponder.saxalizer.AccessMethod;
import uk.org.ponder.saxalizer.SAXalizerMappingContext;
import uk.org.ponder.saxalizer.support.MethodAnalyser;
import uk.org.ponder.util.Logger;

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

  // If this is set, will be used in preference to applicationContext to resolve
  // beans
  private RSACBeanLocator rsacbeanlocator;

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
    for (int i = 0; i < limit; ++i) {
      TargetListAggregatingBean tlab = (TargetListAggregatingBean) tlabs.get(i);
      Object bindafter = tlab.getBindAfter();
      if ("*".equals(bindafter)) {
        tlabs.remove(i);
        tlabs.add(tlab);
        --i;
        --limit;
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
      validateTLAB(tlab, viewbean);

      MapUtil.putMultiMap(targetMap, tlab.getTargetBean(), tlab);
    }

    for (Iterator values = targetMap.values().iterator(); values.hasNext();) {
      List tlabs = (List) values.next();
      sortTLABs(tlabs);
    }
  }

  private void validateTLAB(TargetListAggregatingBean tlab, String viewbean) {
    int getters = 0;
    if (tlab.getValue() != null)
      ++getters;
    if (tlab.getValueRef() != null)
      ++getters;
    if (tlab.getValueRefs() != null)
      ++getters;
    if (getters != 1) {
      throw new IllegalArgumentException(
          "Error reading TargetListAggregatingBean " + viewbean
              + ": exactly one of value or valueRef must be set");
    }
  }

  private Object fetchValue(TargetListAggregatingBean tlab, BeanLocator fetcher) {
    Object value = tlab.getValue();
    if (value == null) {
      if (tlab.getValueRef() != null) {
        value = fetcher.locateBean(tlab.getValueRef());
      }
      else {
        List list = new ArrayList();
        String[] beans = tlab.getValueRefs();
        for (int i = 0; i < beans.length; ++i) {
          list.add(fetcher.locateBean(beans[i]));
        }
        value = list;
      }
    }
    return value;
  }

  public void checkGuard(Set loaded) {
    int cloaded = 0;
    for (Iterator keyit = targetMap.keySet().iterator(); keyit.hasNext();) {
      String key = (String) keyit.next();
      if (loaded.contains(key)) {
        Logger.log.error("Bean " + key
            + " which was the target of TLAB definition has already been "
            + "loaded by search for TLAB definitions during startup");
        ++cloaded;
      }
    }
    if (cloaded > 0) {
      throw new IllegalArgumentException(
          cloaded
              + (cloaded > 1 ? " beans which were"
                  : " bean which was")
              + " the target of TLAB definition became loaded"
              + " by search for TLAB definitions during startup - consider breaking"
              + " this cycle by use of valueRef rather than value in the TLAB definition");
    }
  }

  public void setRSACBeanLocator(RSACBeanLocator rsacbeanlocator) {
    this.rsacbeanlocator = rsacbeanlocator;
  }

  public Object postProcessAfterInitialization(Object bean, String beanName) {
    return bean;
  }

  public Object postProcessBeforeInitialization(Object bean, String beanName)
      throws BeansException {
    // Perhaps in Ruby, Perl, or Haskell, this method body is just 4 lines!

    BeanLocator fetcher = rsacbeanlocator == null ? new BeanFactoryBeanLocator(
        applicationContext)
        : (BeanLocator) rsacbeanlocator.getBeanLocator();
    List tlabs = (List) targetMap.get(beanName);
    if (tlabs == null)
      return bean;
    Map listprops = new HashMap(); // map of property name to list
    for (int i = 0; i < tlabs.size(); ++i) {
      TargetListAggregatingBean tlab = (TargetListAggregatingBean) tlabs.get(i);
      Object value = fetchValue(tlab, fetcher);
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
        throw new IllegalArgumentException("TLAB target bean " + beanName
            + " does not have any writeable property named " + propname);
      }
      sam.setChildObject(bean, value);
    }
    return bean;
  }

}
