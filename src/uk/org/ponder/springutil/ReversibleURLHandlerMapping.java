/*
 * Created on Jun 15, 2005
 */
package uk.org.ponder.springutil;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.web.servlet.handler.AbstractHandlerMapping;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.util.UrlPathHelper;

/**
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *  
 */
public class ReversibleURLHandlerMapping extends AbstractHandlerMapping
    implements ReverseURLMapper {
  
  private UrlPathHelper urlPathHelper = new UrlPathHelper();
  
  private Map forwardmap;
  private Map backwardmap;

  /**
   * Set a Map with URL paths as keys and handler beans as values. Convenient
   * for population with bean references.
   * <p>
   * The key MUST be a single URL path (no wildcards), 
   * and the value MUST be a bean implementing
   * Controller.
   * 
   * @param urlMap
   *          map with URLs as keys and beans as values
   */
  public void setUrlMap(Map urlMap) {
    this.forwardmap.putAll(urlMap);
    for (Iterator hit = urlMap.keySet().iterator(); hit.hasNext(); ) {
      String url = (String) hit.next();
      Controller controller = (Controller) urlMap.get(url);
      if (controller instanceof URLAwareController) {
        URLAwareController uc = (URLAwareController)controller;
        uc.setReverseURLMapper(this);
        backwardmap.put(uc.getBeanName(), url);
      }
      
//      backwardmap.put(controller, url);
    }
  }

  public String URLforBean(String beanname) {
    return (String) backwardmap.get(beanname);
  }

  // copied from AbstractUrlHandlerMapping
  /**
	 * Look up a handler for the URL path of the given request.
	 * @param request current HTTP request
	 * @return the looked up handler instance, or null
	 */
  /*
	protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
		String lookupPath = this.urlPathHelper.getLookupPathForRequest(request);
		if (logger.isDebugEnabled()) {
			logger.debug("Looking up handler for [" + lookupPath + "]");
		}
		return lookupHandler(lookupPath);
	}
*/
  
  
  public void initApplicationContext() throws BeansException {
  
  }

  protected Object getHandlerInternal(HttpServletRequest request) throws Exception {
    // TODO Auto-generated method stub
    return null;
  }
}