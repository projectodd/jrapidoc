package org.projectodd.jrapidoc.introspector;

import org.apache.commons.lang3.StringUtils;
import org.projectodd.jrapidoc.annotation.soap.wsprovider.DocParam;
import org.projectodd.jrapidoc.annotation.soap.wsprovider.DocParams;
import org.projectodd.jrapidoc.annotation.soap.wsprovider.DocSOAPBinding;
import org.projectodd.jrapidoc.logger.Logger;
import org.projectodd.jrapidoc.model.Service;
import org.projectodd.jrapidoc.model.ServiceGroup;
import org.projectodd.jrapidoc.model.SoapBinding;
import org.projectodd.jrapidoc.model.TransportType;
import org.projectodd.jrapidoc.model.type.provider.TypeProvider;

import javax.xml.ws.WebServiceProvider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.Set;

public class WSProviderProcessor extends AbstractWSIntrospector{

    public WSProviderProcessor(TypeProvider typeProvider){
        super(typeProvider);
    }

    public ServiceGroup createServiceGroup(Set<Class<?>> providerClasses, ServiceGroup.ServiceGroupBuilder serviceGroupBuilder){
        for (Class<?> providerClass:providerClasses){
            if(!providerClass.isAnnotationPresent(WebServiceProvider.class)){
                continue;
            }
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
                    resourceBuilder.method(createMethod(method, providerClass));
                    break;
                }
            }
        }
    }

    org.projectodd.jrapidoc.model.Method createMethod(Method method, Class<?> providerClass){
        Logger.debug("{0} method processing started", method.toString());
        org.projectodd.jrapidoc.model.Method.MethodBuilder methodBuilder = new org.projectodd.jrapidoc.model.Method.MethodBuilder();
        methodBuilder.description(getDescription(method.getDeclaredAnnotations()));
        methodBuilder.isAsynchronous(true);
        methodBuilder.name(method.getName());
        addSoapBinding(method, providerClass, methodBuilder);
        addInputHeaders(method, methodBuilder);
        addInputParams(method, methodBuilder);
        org.projectodd.jrapidoc.model.Method jrdMethod = methodBuilder.build();
        Logger.debug("{0} method processing finished", method.toString());
        return jrdMethod;
    }

    void addInputParams(Method method, org.projectodd.jrapidoc.model.Method.MethodBuilder methodBuilder){
        DocParams paramsAnno = method.getAnnotation(DocParams.class);
        DocParam paramAnno = method.getAnnotation(DocParam.class);
        if(paramsAnno != null){
            for (DocParam paramItem:paramsAnno.value()){
                if (!paramItem.isHeader()){
                    addInputParam(paramItem, methodBuilder);
                }
            }
        }else if(paramAnno != null){
            if (!paramAnno.isHeader()){
                addInputParam(paramAnno, methodBuilder);
            }
        }
    }

    void addSoapBinding(Method method, Class<?> seiClass, org.projectodd.jrapidoc.model.Method.MethodBuilder methodBuilder) {
        DocSOAPBinding soapBindingAnno = getAnnotation(method.getDeclaredAnnotations(), DocSOAPBinding.class);
        SoapBinding.SoapBindingBuilder soapBindingBuilder = new SoapBinding.SoapBindingBuilder();
        if (soapBindingAnno == null) {
            soapBindingAnno = getAnnotation(seiClass.getDeclaredAnnotations(), DocSOAPBinding.class);
        }
        if (soapBindingAnno != null) {
            soapBindingBuilder.parameterStyle(soapBindingAnno.parameterStyle().name()).style(soapBindingAnno.style().name())
                    .use(soapBindingAnno.use().name()).build();
        }
        methodBuilder.soapBinding(soapBindingBuilder.build());
    }

    void addInputHeaders(Method method, org.projectodd.jrapidoc.model.Method.MethodBuilder methodBuilder) {
        DocParams paramsAnno = method.getAnnotation(DocParams.class);
        DocParam paramAnno = method.getAnnotation(DocParam.class);
        if(paramsAnno != null){
            for (DocParam paramItem:paramsAnno.value()){
                if (paramItem.isHeader()){
                    addInputHeader(paramItem, methodBuilder);
                }
            }
        }else if(paramAnno != null){
            if (paramAnno.isHeader()){
                addInputHeader(paramAnno, methodBuilder);
            }
        }
    }

    void addInputHeader(DocParam docParam, org.projectodd.jrapidoc.model.Method.MethodBuilder methodBuilder){
        TransportType transportType = createTransportType(docParam);
        methodBuilder.soapInputHeader(transportType);
    }

    void addInputParam(DocParam docParam, org.projectodd.jrapidoc.model.Method.MethodBuilder methodBuilder){
        TransportType transportType = createTransportType(docParam);
        methodBuilder.parameter(transportType);
    }

    private TransportType createTransportType(DocParam docParam) {
        TransportType.TransportTypeBuilder transportTypeBuilder = new TransportType.TransportTypeBuilder();
        transportTypeBuilder.description(docParam.description());
        transportTypeBuilder.isRequired(docParam.isRequired());
        transportTypeBuilder.type(createType(docParam.type()));
        return transportTypeBuilder.build();
    }
}
