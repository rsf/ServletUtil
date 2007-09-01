/*
 * Created on 25 Jul 2006
 */
package uk.org.ponder.springutil;

/**
 * A static, that is to say, concrete, implementation of the 
 * {@link TargetListAggregatingBean} marker definition. 
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */

public class StaticTLAB implements TargetListAggregatingBean {
  private String targetBean;
  private String targetProperty;
  private Object value;
  private boolean unwraplists = true;
  private Object bindAfter;
  private Object bindBefore;
  private String valueref;

  /**
   * The name of the target bean to receive the held bean as part of its list
   * dependency
   */
  public void setTargetBean(String targetBean) {
    this.targetBean = targetBean;
  }

  public String getTargetBean() {
    return targetBean;
  }

  /** The name of the list-valued property of the target bean * */
  public void setTargetProperty(String targetProperty) {
    this.targetProperty = targetProperty;
  }

  public String getTargetProperty() {
    return targetProperty;
  }

  /**
   * A compact alternative to specifying <code>targetBean</code> and
   * <code>targetProperty</code> - use <code>targetPath</code> instead to
   * specify a single dot-separated "EL" as
   * <code>targetBean.targetProperty</code>
   */
  public void setTargetPath(String targetPath) {
    int dotpos = targetPath.indexOf('.');
    if (dotpos == -1) {
      throw new IllegalArgumentException("target path " + targetPath
          + " must contain a dot");
    }
    targetBean = targetPath.substring(0, dotpos);
    targetProperty = targetPath.substring(dotpos + 1);
  }

  /**
   * The "held" object to be aggregated into the list-valued property of the
   * target bean. Exactly one out of this property and {@link #setValueRef(String)} 
   * must be set.
   */
  public void setValue(Object value) {
    this.value = value;
  }

  public Object getValue() {
    return value;
  }
  
  /** The EL or bean name at which the "held" object can be found. Exactly
   * one out of this property and {@link #setValue(Object)} must be set.
   */
  
  public void setValueRef(String valueref) {
    this.valueref = valueref;
  }
  
  public String getValueRef() {
    return valueref;
  }

  /**
   * If set to <code>false</code> (the default is <code>true</code>, any
   * list-valued object will be delivered "in the raw" into the target property
   * list, rather than being unpacked into it.
   */

  public void setUnwrapLists(boolean unwraplists) {
    this.unwraplists = unwraplists;
  }

  public boolean getUnwrapLists() {
    return unwraplists;
  }

  /** If set to "*" will cause the contributed bean to be placed in the first
   * list position. Other values will be supported in future versions.
   */
  public void setBindBefore(Object bindBefore) {
    this.bindBefore = bindBefore;
  }

  /** If set to "*" will cause the contributed bean to be placed in the last
   * list position. Other values will be supported in future versions.
   */
  public void setBindAfter(Object bindAfter) {
    this.bindAfter = bindAfter;
  }

  public Object getBindAfter() {
    return bindAfter;
  }

  public Object getBindBefore() {
    return bindBefore;
  }
}
