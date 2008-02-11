/*
 * Created on 1 Sep 2007
 */
package uk.org.ponder.rsac;

import uk.org.ponder.beanutil.IterableWriteableBeanLocator;
import uk.org.ponder.beanutil.WriteableBeanLocator;

/**
 * The central class managing the Request Scope Application Context.
 * <p>
 * The principal method useful to users is <code>getBeanLocator()</code> which
 * returns a BeanLocator holding the request context for the current thread. For
 * each thread entering an RSAC request, it must call
 * <code>startRequest()</code> before acquiring any request beans, and
 * <code>endRequest()</code> at the end of its cycle (the latter is most
 * important).
 */

public interface RSACBeanLocator {
  /** The standard bean name for the RSACBeanLocator * */
  public static String RSAC_BEAN_LOCATOR_NAME = "RSACBeanLocator";
  
  /**
   * Starts the request-scope container for the current thread.
   */

  public void startRequest();

  /**
   * Determines whether the container has already been started for the current
   * thread.
   */

  public boolean isStarted();

  /**
   * Called at the end of a request. I advise doing this in a finally block.
   */
  public void endRequest();

  /**
   * Returns a list of bean names which are known to correspond to beans
   * implementing or derived from the supplied class. RSAC has tried slightly
   * harder to resolve bean classes than Spring generally does, through walking
   * chains of factory-methods.
   * 
   * @param clazz A class or interface class to be searched for.
   * @return A list of derived bean names.
   */
  public String[] beanNamesForClass(Class clazz);

  /**
   * Returns the class of this bean, if it can be statically determined,
   * <code>null</code> if it cannot (i.e. this bean is the product of a
   * factory-method of a class which is not yet known)
   * 
   * @param beanname
   * @return
   */
  public Class getBeanClass(String beanname);

  /**
   * This method gets a BeanLocator which is good just for the current request
   * scope. The ThreadLocal barrier has already been breached in the returned
   * object, and evaluation will proceed quickly.
   */
  public WriteableBeanLocator getBeanLocator();

  /**
   * Scope of this BeanLocator is the same as previous, but it will NOT
   * auto-create beans that are not present.
   */
  public IterableWriteableBeanLocator getDeadBeanLocator();

}