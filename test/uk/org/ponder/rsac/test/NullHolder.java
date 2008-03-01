/*
 * Created on 29 Feb 2008
 */
package uk.org.ponder.rsac.test;

public class NullHolder {
  public void setNullable(String nullable) {
    if (nullable != null) {
      throw new IllegalArgumentException("Null not correctly delivered");
    }
  }
}
