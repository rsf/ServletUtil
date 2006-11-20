/*
 * Created on 17-Dec-2005
 */
package uk.org.ponder.rsac;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanInitializationException;

import uk.org.ponder.beanutil.ConcreteWBL;
import uk.org.ponder.beanutil.WriteableBeanLocator;
import uk.org.ponder.springutil.BeanLocatorBeanFactory;
import uk.org.ponder.stringutil.StringList;
import uk.org.ponder.util.UniversalRuntimeException;

class PerRequestInfo {
  // HashMap beans = new HashMap();
  int cbeans = 0;
  ConcreteWBL beans = new ConcreteWBL(); // the raw bean container
  WriteableBeanLocator requestwbl; // "active" container with lazy-init
  ArrayList postprocessors = new ArrayList();
  StringList todestroy = new StringList();
  // a cached BeanFactory corresponding to the lazy container, for any
  // BeanFactoryAware beans
  BeanFactory blfactory;
  // the container of RSACLazyTargetSources, permanent in this ThreadLocal.
  Map lazysources;
  Map seedbeans = new HashMap();

  public void clear() {
    cbeans = 0;
    // we now know that all of this stuff is actually SLOWER than throwing the
    // whole entry away. But we NEED to cache the lazytargets, so what the
    // heck...
    beans.clear();
    todestroy.clear();
    postprocessors.clear();
  }

  public PerRequestInfo(final RSACBeanLocator rsacbl, StringList lazysources) {

    requestwbl = new WriteableBeanLocator() {
      public Object locateBean(String beanname) {
        return rsacbl.getBean(PerRequestInfo.this, beanname, false);
      }

      public boolean remove(String beanname) {
        return beans.remove(beanname);
      }

      public void set(String beanname, Object toset) {
        seedbeans.put(beanname, toset);
        beans.set(beanname, toset);
      }
    };

    HashMap thislazies = new HashMap();
    blfactory = new BeanLocatorBeanFactory(requestwbl);
    for (int i = 0; i < lazysources.size(); ++i) {
      String lazysource = lazysources.stringAt(i);
      try {
        ProxyFactoryBean pfb = new ProxyFactoryBean();

        Class beanclass = rsacbl.getBeanClass(lazysource);
        if (beanclass == null) {
          throw UniversalRuntimeException.accumulate(
              new BeanInitializationException(""),
              "Unable to determine the class of bean " + lazysource
                  + " which has bean marked as (very) lazy");
        }
        // NB - report as bug! This proxies NOTHING if the supplied class
        // is not a concrete class.
        RSACLazyTargetSource rlts = new RSACLazyTargetSource(rsacbl, this,
            beanclass, lazysource);
        if (beanclass.isInterface()) {
          pfb.setInterfaces(new Class[] { beanclass });
        }
        else {
          pfb.setProxyTargetClass(true);
        }
        pfb.setTargetSource(rlts);
        pfb.setBeanFactory(blfactory);

        thislazies.put(lazysource, pfb);
      }
      catch (Exception e) {
        throw UniversalRuntimeException.accumulate(e,
            "Error constructing Bridge proxy for bean name " + lazysource);
      }
    }
    this.lazysources = thislazies;

  }
}