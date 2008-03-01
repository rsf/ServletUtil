/*
 * Created on 29 Feb 2008
 */
package uk.org.ponder.rsac.test;

public class TestRSAC extends AbstractRSACTests {

  public TestRSAC() {
    contributeRequestConfigLocation("classpath:uk/org/ponder/rsac/test/rsac-request-context.xml");
    contributeConfigLocation("classpath:conf/core-rsac-context.xml");
  }
  
  public void testRSAC() {
    locateRequestBean("nullTest");
    
    try {
      locateRequestBean("invalidClassTest");
    }
    catch (Exception e) {
      assertTrue(e.getMessage().contains("uk.org.ponder.InvalidClass"));
    }
  }
  
}
