/*
 * Created on 25 Jul 2006
 */
package uk.org.ponder.springutil;

/** Collects together a number of beans (probably implementing some common
 * interface), and delivers them as a list dependency to a nominated "target bean".
 * This will occur as part of the PostProcessing of the target bean, which doesn't
 * need to be made aware of the process explicitly. 
 * <p>Note that you could cut down on verbosity a bit by making the "value" bean
 * an inner bean definition of the TLAB definition itself.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class StaticTLAB {
  private String targetBean;
  private String targetProperty;
  private Object value;
  private boolean unwraplists = true;

  /** The name of the target bean to receive the held bean as part of its
   * list dependency 
   */
  public void setTargetBean(String targetBean) {
    this.targetBean = targetBean;
  }
  
  public String getTargetBean() {
    return targetBean;
  }
  
  /** The name of the list-valued property of the target bean **/
  public void setTargetProperty(String targetProperty) {
    this.targetProperty = targetProperty;
  }
  
  public String getTargetProperty() {
    return targetProperty;
  }
  
  /** A compact alternative to specifying <code>targetBean</code> and 
   * <code>targetProperty</code> - use <code>targetPath</code> instead to specify
   * a single dot-separated "EL" as <code>targetBean.targetProperty</code>
   */
  public void setTargetPath(String targetPath) {
    int dotpos = targetPath.indexOf('.');
    if (dotpos == -1) {
      throw new IllegalArgumentException("target path " + targetPath + " must contain a dot");
    }
    targetBean = targetPath.substring(0, dotpos);
    targetProperty = targetPath.substring(dotpos + 1);
  }
  
  /** The "held" object to be aggregated into the list-valued property of the
   * target bean.
   */ 
  public void setValue(Object value) {
    this.value = value;
  }
  
  public Object getValue() {
    return value;
  }
  /** If set to <code>false</code> (the default is <code>true</code>,
   * any list-valued object will be delivered "in the raw" into the target 
   * property list, rather than being unpacked into it.
   */
  
  public void setUnwrapLists(boolean unwraplists) {
    this.unwraplists = unwraplists;
  }
  
  public boolean getUnwrapLists() {
    return unwraplists;
  }
}
