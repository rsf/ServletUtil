/*
 * Created on 12 Sep 2007
 */
package uk.org.ponder.springutil.validator.support;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import uk.org.ponder.beanutil.BeanModelAlterer;
import uk.org.ponder.springutil.validator.ValidatorArgReporter;
import uk.org.ponder.springutil.validator.ValidatorCodeReceiver;
import uk.org.ponder.springutil.validator.ValidatorCompostor;
import uk.org.ponder.springutil.validator.ValidatorFactory;
import uk.org.ponder.stringutil.StringUtil;

/**
 * Parses validators out of a primitive String specification into a concrete
 * implementation (currently) as a Spring validator.
 * 
 * @author Antranig Basman (antranig@caret.cam.ac.uk)
 */

public class ValidatorCompostorImpl implements ValidatorCompostor {
  private List validators;
  private Map nametoval = new HashMap();

  public void setBeanModelAlterer(BeanModelAlterer beanModelAlterer) {
    this.beanModelAlterer = beanModelAlterer;
  }

  public void setValidators(List validators) {
    this.validators = validators;
  }

  private BeanModelAlterer beanModelAlterer;

  public void init() {
    for (int i = 0; i < validators.size(); ++i) {
      ValidatorFactory vf = (ValidatorFactory) validators.get(i);
      String name = vf.getName();
      nametoval.put(name, vf);
    }
  }

  private void sortValidators(ValidatorBase[] vals) {
    Arrays.sort(vals, new Comparator() {

      public int compare(Object arg0, Object arg1) {
        ValidatorBase val1 = (ValidatorBase) arg0;
        ValidatorBase val2 = (ValidatorBase) arg1;
        return val1.getPriority() - val2.getPriority();
      }
    });
  }

  public Validator parseValidator(String spec) {
    int colpos = spec.indexOf(':');
    String offset = null;
    if (colpos != -1) {
      offset = spec.substring(0, colpos);
      spec = spec.substring(colpos + 1);
    }
    String[] segments = StringUtil.parseArray(spec);
    final ValidatorBase[] vals = new ValidatorBase[segments.length];
    for (int i = 0; i < segments.length; ++i) {
      ValidatorBase val = parseSingleValidator(segments[i]);
      val.setOffsetPath(offset);
      vals[i] = val;
    }
    sortValidators(vals);
    return new Validator() {
      public boolean supports(Class clazz) {
        return true;
      }

      public void validate(Object obj, Errors errors) {
        boolean failed = false;
        for (int i = 0; i < vals.length; ++i) {
          if (i > 0 && failed
              && vals[i].getPriority() > vals[i - 1].getPriority()) {
            break;
          }
          String offsetpath = vals[i].getOffsetPath();
          if (offsetpath != null) {
            Object offset = beanModelAlterer
                .getBeanValue(offsetpath, obj, null);
            errors.pushNestedPath(offsetpath);
            try {
              int oldcount = errors.getErrorCount();
              vals[i].validate(offset, errors);
              boolean thisfail = errors.getErrorCount() > oldcount;
              failed = failed | thisfail;
            }
            finally {
              errors.popNestedPath();
            }
          }
          else {
            vals[i].validate(obj, errors);
          }
        }
      }
    };
  }

  private ValidatorBase parseSingleValidator(String spec) {
    int brackpos = spec.indexOf('(');
    String name = spec.trim();
    String[] args = new String[] {};
    if (brackpos != -1) {
      name = spec.substring(0, brackpos);
      int lastbrackpos = spec.lastIndexOf(')');
      args = StringUtil
          .parseArray((spec.substring(brackpos + 1, lastbrackpos)));
    }
    ValidatorFactory vnr = (ValidatorFactory) nametoval.get(name);
    Object validator = vnr.getValidator();
    int setterargs = 0;
    if (vnr instanceof ValidatorArgReporter) {
      String setterspec = ((ValidatorArgReporter) vnr)
          .getValidatorArgMethods();
      String[] setters = StringUtil.parseArray(setterspec);
      setterargs = setters.length;
      for (int i = 0; i < setters.length; ++i) {
        beanModelAlterer.setBeanValue(setters[i], validator, args[i], null,
            true);
      }
    }
    if (validator instanceof ValidatorCodeReceiver) {
      ValidatorCodeReceiver vcr = (ValidatorCodeReceiver) validator;
      if (args.length > setterargs) {
        vcr.setMessageCode(args[args.length - 1]);
      }
      vcr.setMessageArgs(args);
    }
    if (validator instanceof ValidatorBase) {
      ValidatorBase base = (ValidatorBase) validator;
      base.setPriority(vnr.getPriority());
    }
    // Currently only supports Spring validators
    return (ValidatorBase) validator;
  }
}
