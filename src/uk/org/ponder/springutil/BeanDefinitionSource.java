/*
 * Created on 27-Feb-2006
 */
package uk.org.ponder.springutil;

/** An implementation of part of the non-bean-getting interface of
 * DefaultListableBeanFactory. Unfortunately all the good names are already
 * taken by Spring!
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */

public interface BeanDefinitionSource {

  /**
   * Returns a list of bean names which are known to correspond to beans
   * implementing or derived from the supplied class. RSAC has tried slightly
   * harder to resolve bean classes than Spring generally does, through walking
   * chains of factory-methods.
   * 
   * @param clazz A class or interface class to be searched for.
   * @return A list of derived bean names.
   */
  public abstract String[] beanNamesForClass(Class clazz);

  /**
   * Returns the class of this bean, if it can be statically determined,
   * <code>null</code> if it cannot (i.e. this bean is the product of a
   * factory-method of a class which is not yet known).
   * 
   * @param beanname
   * @return
   */
  public abstract Class getBeanClass(String beanname);

}