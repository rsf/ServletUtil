/*
 * Created on May 5, 2006
 */
package uk.org.ponder.springutil;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.MultipartResolver;

/** A default MultipartResolver with no behaviour, to avoid build dependencies
 * for those who do not require upload support.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */

public class BlankMultipartResolver implements MultipartResolver {
  public boolean isMultipart(HttpServletRequest request) {
    return false;
  }

  public MultipartHttpServletRequest resolveMultipart(HttpServletRequest request){
    return null;
  }

  public void cleanupMultipart(MultipartHttpServletRequest request) {
  }

}
