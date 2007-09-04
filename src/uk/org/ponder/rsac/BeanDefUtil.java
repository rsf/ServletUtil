/*
 * Created on 04-Feb-2006
 */

// Some code in this file is subject to the ASF licence, terms follow:
/*
 * Copyright 2002-2004 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// Under term 4c) of the licence, attribution details taken from the original
// source (AbstractBeanFactory.java) are as follows:
// * @author Rod Johnson
// * @author Juergen Hoeller
// * @since 15 April 2001
package uk.org.ponder.rsac;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.PropertyValue;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.ConstructorArgumentValues;
import org.springframework.beans.factory.config.RuntimeBeanReference;
import org.springframework.beans.factory.config.TypedStringValue;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.support.RootBeanDefinition;

import uk.org.ponder.conversion.StringArrayParser;
import uk.org.ponder.reflect.ClassGetter;
import uk.org.ponder.reflect.ReflectUtils;
import uk.org.ponder.saxalizer.AccessMethod;
import uk.org.ponder.saxalizer.MethodAnalyser;
import uk.org.ponder.saxalizer.SAXAccessMethod;
import uk.org.ponder.stringutil.StringList;
import uk.org.ponder.util.Logger;

public class BeanDefUtil {

  // NB an IMPORTANT CHANGE from the ABF version is change of references to
  // AbstractBeanFactory into ConfigurableListableBeanFactory. This considerably
  // weakens the type generality of our system but is unavoidable since
  // CLBF is the LOWEST level at which a getBeanDefinition() method is visible
  // to us, since we are no longer members of ABF. Since in practice we plan
  // to implement all RSAC using GenericApplicationContexts, this is not a
  // problem, and anyone who wants to inherit bean definitions FROM application
  // scope INTO request scope is i) deranged and ii) gets everything they
  // deserve.
  static AbstractBeanDefinition getMergedBeanDefinition(
      ConfigurableListableBeanFactory factory, String beanName,
      boolean includingAncestors) throws BeansException {
    try {
      return getMergedBeanDefinition(factory, beanName, 
          factory.getBeanDefinition(beanName));
    }
    catch (NoSuchBeanDefinitionException ex) {
      if (includingAncestors
          && factory.getParentBeanFactory() instanceof ConfigurableListableBeanFactory) {
        return getMergedBeanDefinition(
            (ConfigurableListableBeanFactory) factory.getParentBeanFactory(),
            beanName, true);
      }
      else {
        throw ex;
      }
    }
  }
// Need resistance for incompatibility in signature of BeanDefinition which 
// has appeared in Spring 2.1 - this now features the getParentName method which
// used only to appear in ChildBeanDefinition. There is now also "GenericBeanDefinition"
// which is inherited off the common parent AbstractBeanDefinition.
  static String getParentDefinitionName(BeanDefinition bd) {
    try {
      if (ReflectUtils.hasMethod(bd, "getParentName")) {
        Method method = bd.getClass().getMethod("getParentName",
            SAXAccessMethod.emptyclazz);
        return (String) method.invoke(bd, SAXAccessMethod.emptyobj);
      }
    }
    catch (Exception e) {
    }
    return null;
  }

  static AbstractBeanDefinition getMergedBeanDefinition(
      ConfigurableListableBeanFactory factory, String beanName,
      BeanDefinition bd) throws BeansException {

    if (!(bd instanceof AbstractBeanDefinition)) {
      throw new IllegalArgumentException("Bean definition " + beanName + " of " + bd.getClass() + 
          " which is not descended from AbstractBeanDefinition can not be recognised");
    }
    AbstractBeanDefinition abd = (AbstractBeanDefinition) bd;
    String parentName = getParentDefinitionName(abd);
    if (parentName == null) {
      return abd;
    }
    else  {
      AbstractBeanDefinition pbd = null;
      if (!beanName.equals(parentName)) {
        pbd = getMergedBeanDefinition(factory, parentName, true);
      }
      else {
        if (factory.getParentBeanFactory() instanceof ConfigurableListableBeanFactory) {
          ConfigurableListableBeanFactory parentFactory = (ConfigurableListableBeanFactory) factory
              .getParentBeanFactory();
          pbd = getMergedBeanDefinition(parentFactory, parentName,
              true);
        }
        else {
          throw new NoSuchBeanDefinitionException(
              parentName,
              "Parent name '"
                  + parentName
                  + "' is equal to bean name '"
                  + beanName
                  + "' - cannot be resolved without an AbstractBeanFactory parent");
        }
      }

      // deep copy with overridden values
      pbd.overrideFrom(abd);
      return pbd;
    }
   
  }

  static RSACBeanInfo convertBeanDef(BeanDefinition origdef, String beanname,
      ConfigurableListableBeanFactory factory, MethodAnalyser abdAnalyser,
      BeanDefConverter converter) {
    RSACBeanInfo rbi = new RSACBeanInfo();
    AbstractBeanDefinition def = getMergedBeanDefinition(factory, beanname,
        origdef);
    MutablePropertyValues pvs = def.getPropertyValues();
    PropertyValue[] values = pvs.getPropertyValues();
    for (int j = 0; j < values.length; ++j) {
      PropertyValue thispv = values[j];
      Object beannames = BeanDefUtil.propertyValueToBeanName(thispv.getValue(),
          converter);
      boolean skip = false;
      // skip recording the dependency if it was unresolvable (some
      // unrecognised
      // type) or was a single-valued type referring to a static dependency.
      // NB - we now record ALL dependencies - bean-copying strategy
      // discontinued.
      if (beannames == null
      // || beannames instanceof String
      // && !blankcontext.containsBean((String) beannames)
      ) {
        skip = true;
      }
      if (!skip) {
        rbi.recordDependency(thispv.getName(), beannames);
      }
    }
    // NB - illegal cast here is unavoidable.
    // Bit of a problem here with Spring flow - apparently the bean class
    // will NOT be set for a "factory-method" bean UNTIL it has been
    // instantiated
    // via the logic in AbstractAutowireCapableBeanFactory l.376:
    // protected BeanWrapper instantiateUsingFactoryMethod(
    AbstractBeanDefinition abd = def;
    rbi.factorybean = abd.getFactoryBeanName();
    rbi.factorymethod = abd.getFactoryMethodName();
    rbi.initmethod = abd.getInitMethodName();
    rbi.destroymethod = abd.getDestroyMethodName();
    rbi.islazyinit = abd.isLazyInit();
    rbi.dependson = abd.getDependsOn();
    rbi.issingleton = abd.isSingleton();
    rbi.isabstract = abd.isAbstract();
    rbi.aliases = factory.containsBeanDefinition(beanname) ? factory
        .getAliases(beanname)
        : StringArrayParser.EMPTY_STRINGL;
    if (abd.hasConstructorArgumentValues()) {
      ConstructorArgumentValues cav = abd.getConstructorArgumentValues();
      boolean hasgeneric = !cav.getGenericArgumentValues().isEmpty();
      boolean hasindexed = !cav.getIndexedArgumentValues().isEmpty();
      if (hasgeneric && hasindexed) {
        throw new UnsupportedOperationException(
            "RSAC Bean "
                + beanname
                + " has both indexed and generic constructor arguments, which is not supported");
      }
      if (hasgeneric) {
        List cvalues = cav.getGenericArgumentValues();
        rbi.constructorargvals = new ConstructorArgumentValues.ValueHolder[cvalues
            .size()];
        for (int i = 0; i < cvalues.size(); ++i) {
          rbi.constructorargvals[i] = (ConstructorArgumentValues.ValueHolder) cvalues
              .get(i);
        }
      }
      else if (hasindexed) {
        Map cvalues = cav.getIndexedArgumentValues();
        rbi.constructorargvals = new ConstructorArgumentValues.ValueHolder[cvalues
            .size()];
        for (int i = 0; i < cvalues.size(); ++i) {
          rbi.constructorargvals[i] = (ConstructorArgumentValues.ValueHolder) cvalues
              .get(new Integer(i));
        }
      }
    }
    if (rbi.factorymethod == null) {
      // Core Spring change at 2.0M5 - ALL bean classes are now irrevocably
      // lazy!!
      // Package org.springframework.beans
      // introduced lazy loading (and lazy validation) of bean classes in
      // standard bean factories and bean definition readers
      AccessMethod bcnaccess = abdAnalyser.getAccessMethod("beanClassName");
      if (bcnaccess != null) {
        String bcn = (String) bcnaccess.getChildObject(abd);
        if (bcn != null) {
          rbi.beanclass = ClassGetter.forName(bcn);
        }
      }
      else {
        // all right then BE like that! We'll work out the class later.
        // NB - beandef.getBeanClass() was eliminated around 1.2, we must
        // use the downcast even earlier now.
        rbi.beanclass = abd.getBeanClass();
      }
    }
    return rbi;
  }

  // magic evil code from AbstractBeanFactory l.443 - this is the main reason
  // I abandoned Spring Forms and the like, and it will return to plague us.
  // Just take a look at the constructor for BeanWrapperImpl - one of these
  // is created for EVERY BEAN IN A FACTORY!

  // protected BeanWrapper createBeanWrapper(Object beanInstance) {
  // return (beanInstance != null ? new BeanWrapperImpl(beanInstance) : new
  // BeanWrapperImpl());
  // }

  // this method is really
  // resolveValueIfNecessary **LITE**, we assume all other resolution
  // is done by the parent factory and are ONLY interested in propertyvalues
  // that refer to other beans IN THIS CONTAINER.
  // org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory
  // l.900:
  // protected Object resolveValueIfNecessary(
  // String beanName, RootBeanDefinition mergedBeanDefinition, String argName,
  // Object value)
  // throws BeansException {
  // Since actual values are the rarer case, make THEM the composite ones.

  // returns either a String or StringList of bean names, or a ValueHolder
  public static Object propertyValueToBeanName(Object value,
      BeanDefConverter converter) {
    Object beanspec = null;
    if (value instanceof BeanDefinitionHolder) {
      // Resolve BeanDefinitionHolder: contains BeanDefinition with name and
      // aliases.
      BeanDefinitionHolder bdHolder = (BeanDefinitionHolder) value;
      String beanname = bdHolder.getBeanName();
      converter.convertBeanDef(bdHolder.getBeanDefinition(), beanname, true);
      beanspec = beanname;
    }
    else if (value instanceof BeanDefinition) {
      throw new IllegalArgumentException(
          "No idea what to do with bean definition!");
    }
    else if (value instanceof RuntimeBeanReference) {
      RuntimeBeanReference ref = (RuntimeBeanReference) value;
      beanspec = ref.getBeanName();
    }
    else if (value instanceof ManagedList) {
      List valuelist = (List) value;
      StringList togo = new StringList();
      for (int i = 0; i < valuelist.size(); ++i) {
        String thisbeanname = (String) propertyValueToBeanName(
            valuelist.get(i), converter);
        togo.add(thisbeanname);
      }
      beanspec = togo;
    }
    else if (value instanceof String) {
      beanspec = new ValueHolder((String) value);
    }
    else if (value instanceof TypedStringValue) {
      beanspec = new ValueHolder(((TypedStringValue) value).getValue());
    }
    else {
      Logger.log.warn("RSACBeanLocator Got value " + value
          + " of unknown type " + value.getClass() + ": ignoring");
    }
    return beanspec;
  }

}
