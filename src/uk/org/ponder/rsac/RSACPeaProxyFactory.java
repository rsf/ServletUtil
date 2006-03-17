/*
 * Created on 17-Mar-2006
 */
package uk.org.ponder.rsac;

import java.lang.reflect.Method;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import org.springframework.aop.TargetSource;

/**
 * A "Pea interceptor" that forces the single "get" method of a pea to return
 * "this", in spite of interference from Spring's
 * <code>massageReturnTypeIfNecessary</code> method.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */

public class RSACPeaProxyFactory implements MethodInterceptor {

  private Class targetclass;
  private TargetSource targetsource;

  public void setTargetClass(Class targetclass) {
    this.targetclass = targetclass;
  }

  public void setTargetSource(TargetSource targetsource) {
    this.targetsource = targetsource;
  }

  public RSACPeaProxyFactory(Class targetclass, TargetSource targetsource) {
    this.targetclass = targetclass;
    this.targetsource = targetsource;
  }
  
  public Object getProxy() {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(targetclass);

//    enhancer.setCallbackFilter(new CallbackFilter() {
//
//      public int accept(Method method) {
//        return method.getName().equals("get") ? 1
//            : 0;
//      }
//    });

    enhancer.setCallback(this);
    return enhancer.create();
  }

  public Object intercept(Object obj, Method method, Object[] args,
      MethodProxy proxy) throws Throwable {
    Object target = targetsource.getTarget();
    return proxy.invoke(target, args);
  }

}