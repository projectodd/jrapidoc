package org.jboss.resteasy.spi.metadata;

import org.jboss.resteasy.util.Types;
import org.projectodd.jrapidoc.annotation.DocDescription;
import org.projectodd.jrapidoc.annotation.rest.DocIsRequired;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class ResourceLocator {
    protected ResourceClass resourceClass;
    protected Class<?> returnType;
    protected Type genericReturnType;
    protected Method method;
    protected Method annotatedMethod;
    protected MethodParameter[] params = {};
    protected String fullpath;
    protected String path;
    protected String pathExample;
    protected String description;
    protected List<ReturnOption> returnOptions = new ArrayList<ReturnOption>();

    public ResourceLocator(ResourceClass resourceClass, Method method, Method annotatedMethod) {
        this.resourceClass = resourceClass;
        this.annotatedMethod = annotatedMethod;
        this.method = method;
        // we initialize parameterized types based on the method of the resource class rather than the Method that is actually
        // annotated.  This is so we have the appropriate parameterized type information.
        this.genericReturnType = Types.resolveTypeVariables(resourceClass.getClazz(), method.getGenericReturnType());
        this.returnType = Types.getRawType(genericReturnType);
        this.params = new MethodParameter[method.getParameterTypes().length];
        for (int i = 0; i < method.getParameterTypes().length; i++) {
            DocDescription desc = null;
            DocIsRequired isReq = null;
            Annotation[] annotations = method.getParameterAnnotations()[i];
            for(Annotation a:annotations){
                if(a.annotationType().equals(DocDescription.class)){
                    desc = (DocDescription)a;
                }else if(a.annotationType().equals(DocIsRequired.class)){
                        isReq = (DocIsRequired)a;
                }
            }
            this.params[i] = new MethodParameter(this, method.getParameterTypes()[i], method.getGenericParameterTypes()[i], annotatedMethod.getParameterAnnotations()[i], desc, isReq);
        }
    }

    public void messageBodyCheck() {
        int messageBodyCount = 0;
        for (MethodParameter param : params) {
            if (param.getParamType().equals(Parameter.ParamType.MESSAGE_BODY)) {
                messageBodyCount++;
                if (messageBodyCount > 1) {
                    throw new RuntimeException("Method " + method.getName() + " in " + resourceClass.getClazz().getCanonicalName() + " has more than one message body parameters.");
                }
            }
        }
    }

    public ResourceClass getResourceClass() {
        return resourceClass;
    }

    public Class<?> getReturnType() {
        return returnType;
    }

    public Type getGenericReturnType() {
        return genericReturnType;
    }

    public Method getMethod() {
        return method;
    }

    public Method getAnnotatedMethod() {
        return annotatedMethod;
    }

    public MethodParameter[] getParams() {
        return params;
    }

    public String getFullpath() {
        return fullpath;
    }

    public String getPath() {
        return path;
    }

    public String getPathExample() {
        return pathExample;
    }

    public String getDescription() {
        return description;
    }

    public List<ReturnOption> getReturnOptions() {
        return returnOptions;
    }

    public ReturnOption getResponseObjectByStatus(int status) {
        for (ReturnOption returnOption : returnOptions) {
            if (returnOption.getStatus() == status) {
                return returnOption;
            }
        }
        return null;
    }
}
