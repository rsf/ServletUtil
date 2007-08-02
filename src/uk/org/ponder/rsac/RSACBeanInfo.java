/*
 * Created on 04-Feb-2006
 */
package uk.org.ponder.rsac;

import java.util.HashMap;
import java.util.Iterator;

import org.springframework.beans.factory.config.ConstructorArgumentValues;

class RSACBeanInfo {
  // The ACTUAL class of the bean to be FIRST constructed. The class of the
  // resultant bean may differ for a factory bean.
  Class beanclass;
  String beanname;
  boolean isfactorybean = false;
  boolean issingleton = true;
  boolean isabstract;
  String initmethod;
  String destroymethod;
  String factorybean;
  String factorymethod;
  String[] dependson;
  String[] aliases;
  boolean islazyinit;
  // key is dependent bean name, value is property name.
  // ultimately we will cache introspection info here.
  private HashMap localdepends = new HashMap();
  public ConstructorArgumentValues.ValueHolder[] constructorargvals;
  // each member is either an app-static RunnableWrapper or a reference to
  // one which can be fetched
  public Object[] fetchwrappers;

  public boolean hasDependencies() {
    return !localdepends.isEmpty();
  }

  public void recordDependency(String propertyname, Object beanref) {
    localdepends.put(propertyname, beanref);
  }

  public Iterator dependencies() {
    return localdepends.keySet().iterator();
  }

  public Object beanref(String propertyname) {
    return localdepends.get(propertyname);
  }
}