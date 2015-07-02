package org.jboss.resteasy.spi.metadata;

import org.projectodd.jrapidoc.annotation.DocDescription;
import org.projectodd.jrapidoc.annotation.rest.DocIsRequired;

import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class FieldParameter extends Parameter
{
   protected Field field;

   protected FieldParameter(ResourceClass declaredClass, Field field)
   {
      super(declaredClass, field.getType(), field.getGenericType(), (field.getAnnotation(DocDescription.class) == null)? null:field.getAnnotation(DocDescription.class).value(), (field.getAnnotation(DocIsRequired.class) == null)? null:field.getAnnotation(DocIsRequired.class).value());
      this.field = field;
   }

   @Override
   public AccessibleObject getAccessibleObject()
   {
      return field;
   }

   @Override
   public Annotation[] getAnnotations()
   {
      return field.getAnnotations();
   }

   public Field getField()
   {
      return field;
   }
}
