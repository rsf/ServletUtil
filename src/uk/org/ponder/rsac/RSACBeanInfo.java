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
  boolean isfactorybean = false;
  String initmethod;
  String destroymethod;
  String factorybean;
  String factorymethod;
  boolean islazyinit;
  // key is dependent bean name, value is property name.
  // ultimately we will cache introspection info here.
  private HashMap localdepends = new HashMap();
  public ConstructorArgumentValues constructorargvals;

  public boolean hasDependencies() {
    return !localdepends.isEmpty();
  }

  public void recordDependency(String propertyname, Object beannames) {
    localdepends.put(propertyname, beannames);
  }

  public Iterator dependencies() {
    return localdepends.keySet().iterator();
  }

  public Object beannames(String propertyname) {
    return localdepends.get(propertyname);
  }
}