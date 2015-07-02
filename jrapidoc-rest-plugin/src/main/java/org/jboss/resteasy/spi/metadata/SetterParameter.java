package org.jboss.resteasy.spi.metadata;

import org.projectodd.jrapidoc.annotation.DocDescription;
import org.projectodd.jrapidoc.annotation.rest.DocIsRequired;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class SetterParameter extends Parameter
{
   protected Method setter;
   protected Method annotatedMethod;

   protected SetterParameter(ResourceClass declaredClass, Method setter, Method annotatedMethod)
   {
      super(declaredClass, setter.getParameterTypes()[0], setter.getGenericParameterTypes()[0], (setter.getAnnotation(DocDescription.class) == null)?null:setter.getAnnotation(DocDescription.class).value(), (setter.getAnnotation(DocIsRequired.class) == null)?null:setter.getAnnotation(DocIsRequired.class).value());
      this.setter = setter;
      this.annotatedMethod = annotatedMethod;
   }

   public Method getSetter()
   {
      return setter;
   }

   public Method getAnnotatedMethod()
   {
      return annotatedMethod;
   }

   @Override
   public AccessibleObject getAccessibleObject()
   {
      return setter;
   }

   @Override
   public Annotation[] getAnnotations()
   {
      return annotatedMethod.getAnnotations();
   }

}
