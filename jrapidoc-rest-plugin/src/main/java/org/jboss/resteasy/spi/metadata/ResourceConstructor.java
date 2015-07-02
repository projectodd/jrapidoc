package org.jboss.resteasy.spi.metadata;

import org.projectodd.jrapidoc.annotation.DocDescription;
import org.projectodd.jrapidoc.annotation.rest.DocIsRequired;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class ResourceConstructor
{
   protected ResourceClass resourceClass;
   protected Constructor constructor;
   protected ConstructorParameter[] params = {};

   public ResourceConstructor(ResourceClass resourceClass, Constructor constructor)
   {
      this.resourceClass = resourceClass;
      this.constructor = constructor;
      if (constructor.getParameterTypes() != null)
      {
         this.params = new ConstructorParameter[constructor.getParameterTypes().length];
         for (int i = 0; i < constructor.getParameterTypes().length; i++)
         {
             DocDescription desc = null;
             DocIsRequired isReq = null;
             Annotation[] annotations = constructor.getParameterAnnotations()[i];
             for(Annotation a:annotations){
                 if(a.annotationType().equals(DocDescription.class)){
                     desc = (DocDescription)a;
                 }else if(a.annotationType().equals(DocIsRequired.class)){
                     isReq = (DocIsRequired)a;
                 }
             }
            this.params[i] = new ConstructorParameter(this, constructor.getParameterTypes()[i], constructor.getGenericParameterTypes()[i], constructor.getParameterAnnotations()[i], desc, isReq);
         }
      }
   }

   public ResourceClass getResourceClass()
   {
      return resourceClass;
   }

   public Constructor getConstructor()
   {
      return constructor;
   }

   public ConstructorParameter[] getParams()
   {
      return params;
   }
}
