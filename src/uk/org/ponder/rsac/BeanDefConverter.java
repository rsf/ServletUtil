/*
 * Created on 13 Nov 2006
 */
package uk.org.ponder.rsac;

import org.springframework.beans.factory.config.BeanDefinition;

public interface BeanDefConverter {
   void convertBeanDef(BeanDefinition origdef, String beanname, boolean inner);
}
