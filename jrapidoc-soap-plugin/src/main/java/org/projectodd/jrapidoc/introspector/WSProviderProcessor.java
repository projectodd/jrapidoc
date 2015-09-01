package org.projectodd.jrapidoc.introspector;

import org.apache.commons.lang3.StringUtils;
import org.projectodd.jrapidoc.annotation.DocDescription;
import org.projectodd.jrapidoc.logger.Logger;
import org.projectodd.jrapidoc.model.Service;
import org.projectodd.jrapidoc.model.ServiceGroup;
import org.projectodd.jrapidoc.model.type.provider.TypeProvider;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.WebServiceProvider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

public class WSProviderProcessor extends AbstractWSIntrospector{

    public WSProviderProcessor(TypeProvider typeProvider){
        super(typeProvider);
    }

    public ServiceGroup createServiceGroup(Set<Class<?>> providerClasses, ServiceGroup.ServiceGroupBuilder serviceGroupBuilder){
        for (Class<?> providerClass:providerClasses){
            Logger.info("{0} processing started", providerClass.getCanonicalName());
            Service service = createEndpoint(providerClass);
            serviceGroupBuilder.service(service);
            Logger.info("{0} processing finished", providerClass.getCanonicalName());
        }
        return serviceGroupBuilder.build();
    }

    void addServiceName(Class<?> providerClass, Service.ResourceBuilder resourceBuilder) {
        WebServiceProvider webServiceAnno = getAnnotation(providerClass.getDeclaredAnnotations(), WebServiceProvider.class);
        String serviceName = providerClass.getSimpleName() + "Service";
        if (webServiceAnno != null) {
            if (StringUtils.isNotEmpty(webServiceAnno.serviceName())) {
                serviceName = webServiceAnno.serviceName();
            }
        }
        resourceBuilder.name(serviceName);
    }

    void addMethods(Class<?> providerClass, Service.ResourceBuilder resourceBuilder) {
        for (Method method : providerClass.getDeclaredMethods()) {
            if (Modifier.isPublic(method.getModifiers())) {
                if(method.getName().equals("invoke")){
                    resourceBuilder.method(createMethod(method));
                    break;
                }
            }
        }
    }

    org.projectodd.jrapidoc.model.Method createMethod(Method method){
        Logger.debug("{0} method processing started", method.toString());
        org.projectodd.jrapidoc.model.Method.MethodBuilder methodBuilder = new org.projectodd.jrapidoc.model.Method.MethodBuilder();
        methodBuilder.description(getDescription(method.getDeclaredAnnotations()));
        methodBuilder.isAsynchronous(true);

        org.projectodd.jrapidoc.model.Method jrdMethod = methodBuilder.build();
        Logger.debug("{0} method processing finished", method.toString());
        return jrdMethod;
    }
}
