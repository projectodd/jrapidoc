package org.jboss.resteasy.spi.metadata;

import javax.ws.rs.core.MediaType;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class ResourceMethod extends ResourceLocator
{
   protected Set<String> httpMethods = new HashSet<String>();
   protected String[] produces;
   protected String[] consumes;
   protected boolean asynchronous;


   public ResourceMethod(ResourceClass declaredClass, Method method, Method annotatedMethod)
   {
      super(declaredClass, method, annotatedMethod);
   }

   public Set<String> getHttpMethods()
   {
      return httpMethods;
   }

   public String[] getProduces()
   {
      return produces;
   }

   public String[] getConsumes()
   {
      return consumes;
   }

   public boolean isAsynchronous()
   {
      return asynchronous;
   }
}
