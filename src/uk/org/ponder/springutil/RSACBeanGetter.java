/*
 * Created on Sep 18, 2005
 */
package uk.org.ponder.springutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import org.apache.log4j.Level;
import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

import uk.org.ponder.beanutil.BeanGetter;
import uk.org.ponder.saxalizer.MethodAnalyser;
import uk.org.ponder.saxalizer.SAXAccessMethod;
import uk.org.ponder.saxalizer.SAXalizerMappingContext;
import uk.org.ponder.util.Copiable;
import uk.org.ponder.util.Logger;

public class RSACBeanGetter implements BeanGetter, ApplicationContextAware {
  private ConfigurableApplicationContext blankcontext;
  private ApplicationContext livecontext;
  private SAXalizerMappingContext smc;

  public RSACBeanGetter(ConfigurableApplicationContext context) {
    this.blankcontext = context;
  }

  public void setMappingContext(SAXalizerMappingContext smc) {
    this.smc = smc;
  }

  public void setApplicationContext(ApplicationContext applicationContext) {
    livecontext = applicationContext;
  }

  // magic evil code from AbstractBeanFactory l.443 - this is the main reason
  // I abandoned Spring Forms and the like, and it will return to plague us.
  // Just take a look at the constructor for BeanWrapperImpl - one of these
  // is created for EVERY BEAN IN A FACTORY!

  // protected BeanWrapper createBeanWrapper(Object beanInstance) {
  // return (beanInstance != null ? new BeanWrapperImpl(beanInstance) : new
  // BeanWrapperImpl());
  // }

  private static String propertyValueToBeanName(Object value) {
    String beanname = null;
    if (value instanceof BeanDefinitionHolder) {
      // Resolve BeanDefinitionHolder: contains BeanDefinition with name and
      // aliases.
      BeanDefinitionHolder bdHolder = (BeanDefinitionHolder) value;
      beanname = bdHolder.getBeanName();
    }
    else if (value instanceof BeanDefinition) {
      throw new IllegalArgumentException(
          "No idea what to do with bean definition!");
    }
    else if (value instanceof RuntimeBeanReference) {
      RuntimeBeanReference ref = (RuntimeBeanReference) value;
      beanname = ref.getBeanName();
    }
    return beanname;
  }

  // the static information stored about each bean.
  private static class RSACBeanInfo {
    // key is dependent bean name, value is property name.
    // ultimately we will cache introspection info here.
    private HashMap localdepends = new HashMap();

    public void setDependency(String localbean, String propertyname) {
      localdepends.put(localbean, propertyname);
    }

    public Iterator dependencies() {
      return localdepends.keySet().iterator();
    }

    public String propertyName(String depname) {
      return (String) localdepends.get(depname);
    }
  }

  // this is a map of bean names to RSACBeanInfo
  private HashMap rbimap;

  public void init() {
    // at this point we actually expect that the "Dead" factory is FULLY
    // CREATED. This checks that all dependencies are resolvable (if this
    // has not already been checked by the IDE).
    String[] beanNames = blankcontext.getBeanDefinitionNames();
    ConfigurableListableBeanFactory factory = blankcontext.getBeanFactory();
    // prepare our list of dependencies. this method is really
    // resolveValueIfNecessary **LITE**, we assume all other resolution
    // is done by the parent factory and are ONLY interested in propertyvalues
    // that refer to other beans IN THIS CONTAINER.
    // org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory
    // l.900:
    // protected Object resolveValueIfNecessary(
    // String beanName, RootBeanDefinition mergedBeanDefinition, String argName,
    // Object value)
    // throws BeansException {

    rbimap = new HashMap();

    for (int i = 0; i < beanNames.length; i++) {
      String beanname = beanNames[i];
      RSACBeanInfo rbi = new RSACBeanInfo();
      BeanDefinition def = factory.getBeanDefinition(beanname);
      MutablePropertyValues pvs = def.getPropertyValues();
      PropertyValue[] values = pvs.getPropertyValues();
      for (int j = 0; j < values.length; ++j) {
        PropertyValue thispv = values[j];
        String localname = propertyValueToBeanName(thispv.getValue());
        if (localname != null && blankcontext.containsBean(localname)) {
          rbi.setDependency(localname, thispv.getName());
        }
      }
      rbimap.put(beanname, rbi);
    }
  }

  private ThreadLocal threadlocal = new ThreadLocal();

  private PerRequestInfo getPerRequest() {
    return (PerRequestInfo) threadlocal.get();
  }

  private static class PerRequestInfo {
    HashMap beans = new HashMap();
    ArrayList postprocessors = new ArrayList();
  }

  public void beginRequest() {
    threadlocal.set(new PerRequestInfo());
  }

  public void endRequest() {
    threadlocal.set(null);
  }

  public void addPostProcessor(BeanPostProcessor beanpp) {
    getPerRequest().postprocessors.add(beanpp);
  }

  private Object createBean(PerRequestInfo pri, String beanname) {
    RSACBeanInfo rbi = (RSACBeanInfo) rbimap.get(beanname);
    // Locate the "dead" bean from the genuine Spring context, and clone it
    // as quick as we can - bytecodes might do faster but in the meantime
    // observe that a clone typically costs 1.6 reflective calls so in general
    // this method will win over a reflective solution.
    // NB - all Copiables simply copy dependencies manually for now, no cost.
    Copiable deadbean = (Copiable) livecontext.getBean(beanname);
    // All the same, the following line will cost us close to 1us - unless it
    // invokes manual code!
    Object clonebean = deadbean.copy();
    // iterate over each LOCAL dependency of the bean with given name.
    for (Iterator depit = rbi.dependencies(); depit.hasNext();) {
      String depbeanname = (String) depit.next();
      Object depbean = pri.beans.get(depbeanname);
      if (depbean == null) {
        depbean = createBean(pri, depbeanname);
      }

      String propname = rbi.propertyName(depbeanname);

      MethodAnalyser ma = MethodAnalyser.getMethodAnalyser(deadbean, smc);
      SAXAccessMethod setter = ma.getAccessMethod(propname);
      // Deliver the dependent bean as member. This will at this point
      // OVERWRITE the "dead" bean inherited from the cloned parent.
      // Lose another 500ns here, until we bring on FastClass.
      setter.setChildObject(clonebean, depbean);

    }
    // enter the bean into the req-specific map.
    pri.beans.put(beanname, clonebean);
    processNewBean(pri, beanname, clonebean);
    return clonebean;
  }

  private void processNewBean(PerRequestInfo pri, String beanname,
      Object clonebean) {
    for (int i = 0; i < pri.postprocessors.size(); ++i) {
      BeanPostProcessor beanpp = (BeanPostProcessor) pri.postprocessors.get(i);
      try {
        beanpp.postProcessBeforeInitialization(clonebean, beanname);
        // someday we might put something in between here.
        beanpp.postProcessAfterInitialization(clonebean, beanname);
      }
      catch (Exception e) {
        Logger.log.log(Level.ERROR, "Exception processing bean "
            + clonebean.getClass().getName(), e);
      }
    }

  }

  public Object getBean(String beanname) {
    PerRequestInfo pri = getPerRequest();
    Object bean = pri.beans.get(beanname);
    if (bean == null) {
      bean = createBean(pri, beanname);
    }
    return bean;
  }

}
