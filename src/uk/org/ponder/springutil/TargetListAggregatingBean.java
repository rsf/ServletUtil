/*
 * Created on 7 Aug 2006
 */
package uk.org.ponder.springutil;

/** Collects together a number of beans (probably implementing some common
 * interface), and delivers them as a list dependency to a nominated "target bean".
 * This will occur as part of the PostProcessing of the target bean, which doesn't
 * need to be made aware of the process explicitly. 
 * <p>Note that you could cut down on verbosity a bit by making the "value" bean
 * an inner bean definition of the TLAB definition itself.
 * 
 * See more comments on {@link StaticTLAB}
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public interface TargetListAggregatingBean {

  public String getTargetBean();

  public String getTargetProperty();

  public Object getValue();

  public boolean getUnwrapLists();

  /** Returns the reference to another TLAB that will be used to SORT the
   * resulting aggregated list. THIS TLAB will occur in the list *before* the
   * target named. It is hoped that natural consistency semantics will cause
   * a set of mutually aggregating TLABs to want to consistently use bindBefore
   * or bindAfter, otherwise construction cycles could well occur.
   * 
   * <p>May be either the TLAB itself or the name of its bean.
   */
  public Object getBindBefore();
  
  /** Returns the reference to another TLAB that THIS TLAB will occur *after*
   * in the resulting aggregated list.
   */
  public Object getBindAfter();
}