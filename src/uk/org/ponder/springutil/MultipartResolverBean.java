/*
 * Created on 04-May-2006
 */
package uk.org.ponder.springutil;

import java.util.Map;

import org.springframework.web.multipart.MultipartException;

/** A bean that will deliver a Map of Spring MultipartFile objects, from
 * a request scope where there has been a file upload.
 * @author Antranig Basman (amb26@ponder.org.uk)
 *
 */

public interface MultipartResolverBean {
  public abstract Map getMultipartMap() throws MultipartException;

}