/*
 * Created on 25 Mar 2008
 */
package uk.org.ponder.springutil;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.mock.web.MockHttpServletResponse;

import uk.org.ponder.stringutil.StringGetter;
import uk.org.ponder.util.UniversalRuntimeException;

public class ServletResponseFactory implements StringGetter {

  private MockHttpServletResponse mockResponse = new MockHttpServletResponse();
  
  public ServletResponseFactory() {
    mockResponse.setCharacterEncoding("UTF-8");
  }
  
  public HttpServletResponse getHttpServletResponse() {
    return mockResponse;
  }
  
  public String get() {
    try {
      return mockResponse.getContentAsString();
    }
    catch (UnsupportedEncodingException e) {
      throw UniversalRuntimeException.accumulate(e, "Encoding not found"); 
    }
  }

}
