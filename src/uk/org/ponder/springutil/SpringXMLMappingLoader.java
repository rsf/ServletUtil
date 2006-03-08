/*
 * Created on Nov 23, 2005
 */
package uk.org.ponder.springutil;

import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import uk.org.ponder.saxalizer.mapping.MappingLoadManager;
import uk.org.ponder.saxalizer.mapping.MappingLoader;
import uk.org.ponder.saxalizer.mapping.MappingLoaderList;

/** A Spring-aware loader which scours the context for all Hooznak, everywhere.
 * More precisely, this will locate all beans of type MappingLoader and 
 * MappingLoaderList in the current ApplicationContext, and invoke them to add
 * any mappings they define to the mapping context set as a property in its
 * superclass.
 * <p>Note that in general Spring requires some form of generalised 
 * object list collaboration scheme whereby this sort of task can be achieved
 * in a more environmentally friendly way.
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 *
 */
public class SpringXMLMappingLoader extends MappingLoadManager 
   implements ApplicationContextAware {
  private ApplicationContext applicationContext;
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
  
  public void init() {
    MappingLoaderList loaders = new MappingLoaderList();
    String[] names = applicationContext.getBeanDefinitionNames(MappingLoader.class);
    for (int i = 0; i < names.length; ++ i) {
      MappingLoader loader = (MappingLoader) applicationContext.getBean(names[i]);
      loaders.add(loader);
    }
    names = applicationContext.getBeanDefinitionNames(MappingLoaderList.class);
      for (int i = 0; i < names.length; ++ i) {
      MappingLoaderList loader = (MappingLoaderList) applicationContext.getBean(names[i]);
      loaders.addAll(loader);
    }
    super.setMappingLoaders(loaders);
  }
  
}
