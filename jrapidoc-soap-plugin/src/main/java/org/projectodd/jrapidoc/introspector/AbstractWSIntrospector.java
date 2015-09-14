package org.projectodd.jrapidoc.introspector;

import org.projectodd.jrapidoc.annotation.DocDescription;
import org.projectodd.jrapidoc.model.Service;
import org.projectodd.jrapidoc.model.type.provider.TypeProvider;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public abstract class AbstractWSIntrospector {
    TypeProvider typeProvider;

    public AbstractWSIntrospector(TypeProvider typeProvider) {
        this.typeProvider = typeProvider;
    }

    Service createEndpoint(Class<?> clazz) {
        Service.ResourceBuilder resourceBuilder = new Service.ResourceBuilder();
        addServiceName(clazz, resourceBuilder);
        resourceBuilder.description(getDescription(clazz.getDeclaredAnnotations()));
        addMethods(clazz, resourceBuilder);
        return resourceBuilder.build();
    }

    org.projectodd.jrapidoc.model.object.type.Type createType(Type param) {
        return typeProvider.createType(param);
    }

    <T extends Annotation> T getAnnotation(Annotation[] annotations, Class<T> annotation) {
        for (Annotation a : annotations) {
            if (a.annotationType().equals(annotation)) {
                return (T) a;
            }
        }
        return null;
    }

    String getDescription(Annotation[] annotations) {
        DocDescription docDescription = getAnnotation(annotations, DocDescription.class);
        if (docDescription == null) {
            return null;
        } else {
            return docDescription.value();
        }
    }

    abstract void addServiceName(Class<?> providerClass, Service.ResourceBuilder resourceBuilder);

    abstract void addMethods(Class<?> seiClass, Service.ResourceBuilder resourceBuilder);
}
