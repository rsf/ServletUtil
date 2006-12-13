/*
 * Created on 20 Aug 2006
 */
package uk.org.ponder.springutil;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;

import uk.org.ponder.saxalizer.SAXalizerMappingContext;

/**
 * Bootstraps the construction of the TLABPostProcessor. This is strictly
 * speaking *ABUSE* of the Spring initialization phase, noting that
 * ApplicationListeners are fetched by AbstractApplicationContext *strictly
 * after* all BeanPostProcessors. The mere *construction* of this listener will
 * cause the PostProcessor to be registered. Therefore its remit is relaxed from
 * post-processing *all* beans in the container, to only those which are
 * constructed during the preInstantiateSingletons phase.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 * 
 */
// This initialization is done late in order to avoid constructing beans
// during the PostProcessor construction phase of Spring, which will
// otherwise trigger the warning:
// INFO: Bean 'reflectiveCache' is not eligible for getting processed by all
// BeanPostProcessors (for example: not eligible for auto-proxying)
public class TLABLoaderListener implements ApplicationListener,
    ApplicationContextAware, InitializingBean {

  private SAXalizerMappingContext mappingContext;
  private ApplicationContext applicationContext;

  public void onApplicationEvent(ApplicationEvent event) {
    // We do not actually respond to any events, purpose is to time
    // initialisation only.
  }

  public void setMappingContext(SAXalizerMappingContext mappingContext) {
    this.mappingContext = mappingContext;
  }

  public void setApplicationContext(ApplicationContext applicationContext) {
    this.applicationContext = applicationContext;
  }

  public void afterPropertiesSet() throws Exception {
    TLABPostProcessor processor = new TLABPostProcessor();
    processor.setApplicationContext(applicationContext);
    processor.setMappingContext(mappingContext == null? SAXalizerMappingContext.instance() : mappingContext);
    ConfigurableApplicationContext cac = (ConfigurableApplicationContext) applicationContext;
    ((ConfigurableBeanFactory) cac.getBeanFactory())
        .addBeanPostProcessor(processor);
  }

}
