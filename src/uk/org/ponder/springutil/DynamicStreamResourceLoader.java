package uk.org.ponder.springutil;

import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.InputStreamResource;

import java.io.InputStream;

/*
 * @author Raymond Chan (raymond@caret.cam.ac.uk)
 * 
 * This bean exists to connect up an InputStream generated at request-time
 * to the XMLViewResolver, which will render the XML Component Tree using
 * the IKAT engine. 
 * 
 * This should be a Request-Scope bean so it that will be thread-safe.
 * It can then be wired up to the Application-Scope XMLViewResolver bean
 * via an RSACBridgeProxy bean.
 */ 
public class DynamicStreamResourceLoader implements ResourceLoader {

  private InputStream inputstr = null;

  /*
   * Call this in your own code once you have an InputStream
   * that will emit an XML Component tree.
   */
  public void setInputStream(InputStream inputstr) {
    this.inputstr = inputstr;
  }

  /*
   * This returns a spring InputStreamResource constructed from
   * the InputStream set earlier. Normally this will be called by
   * the XMLViewResolver bean rather than your own code.
   * It gets passed a String which normally identifies which
   * 'resource' is being requested (e.g the filename of a static XML
   * file on disc); but we always return the InputStream that is
   * currently stored.
   */
  public Resource getResource(String wedontcarewhatthisis) {
    return new InputStreamResource (inputstr);
  }
  
}

