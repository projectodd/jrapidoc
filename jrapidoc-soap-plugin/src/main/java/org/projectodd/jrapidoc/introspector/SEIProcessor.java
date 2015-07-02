package org.projectodd.jrapidoc.introspector;

import org.apache.commons.lang3.StringUtils;
import org.projectodd.jrapidoc.annotation.DocDescription;
import org.projectodd.jrapidoc.annotation.rest.DocIsRequired;
import org.projectodd.jrapidoc.annotation.soap.DocReturn;
import org.projectodd.jrapidoc.annotation.soap.DocReturns;
import org.projectodd.jrapidoc.exception.JrapidocExecutionException;
import org.projectodd.jrapidoc.logger.Logger;
import org.projectodd.jrapidoc.model.*;
import org.projectodd.jrapidoc.model.param.HeaderParam;
import org.projectodd.jrapidoc.model.type.provider.TypeProvider;

import javax.jws.*;
import javax.jws.soap.SOAPBinding;
import javax.xml.ws.Holder;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by Tomas "sarzwest" Jiricek on 7.4.15.
 */
public class SEIProcessor {

    TypeProvider typeProvider;
    ClassLoader loader;

    public SEIProcessor(TypeProvider typeProvider, ClassLoader loader) {
        this.typeProvider = typeProvider;
        this.loader = loader;
    }

    public ServiceGroup createServiceGroup(Set<Class<?>> seiClasses, ServiceGroup.ServiceGroupBuilder serviceGroupBuilder) throws JrapidocExecutionException {
        for (Class<?> seiClass : seiClasses) {
            Logger.info("{0} processing started", seiClass.getCanonicalName());
            seiClass = getSEI(seiClass);
            Service service = createEndpoint(seiClass);
            serviceGroupBuilder.service(service);
            Logger.info("{0} processing finished", seiClass.getCanonicalName());
        }
        return serviceGroupBuilder.build();
    }

    Class<?> getSEI(Class<?> seiClass) throws JrapidocExecutionException {
        String interfaceClass = seiClass.getAnnotation(WebService.class).endpointInterface();
        if (StringUtils.isNotEmpty(interfaceClass)) {
            try {
                return loader.loadClass(interfaceClass);
            } catch (ClassNotFoundException e) {
                Logger.error("Endpoint interface {0} for {1} is not on project classpath", interfaceClass, seiClass.getCanonicalName());
                throw new JrapidocExecutionException(e.getMessage(), e);
            }
        } else {
            return seiClass;
        }
    }

    Service createEndpoint(Class<?> seiClass) {
        Service.ResourceBuilder resourceBuilder = new Service.ResourceBuilder();
        addServiceName(seiClass, resourceBuilder);
        resourceBuilder.description(getDescription(seiClass.getDeclaredAnnotations()));
        addMethods(seiClass, resourceBuilder);
        return resourceBuilder.build();
    }

    void addMethods(Class<?> seiClass, Service.ResourceBuilder resourceBuilder) {
        for (Method method : seiClass.getDeclaredMethods()) {
            if (Modifier.isPublic(method.getModifiers())) {
                WebMethod webMetAnnotation = method.getAnnotation(WebMethod.class);
                if (webMetAnnotation == null) {
                    resourceBuilder.method(createMethod(method, seiClass));
                } else if (!method.getAnnotation(WebMethod.class).exclude()) {
                    resourceBuilder.method(createMethod(method, seiClass));
                }
            }
        }
    }

    String getDescription(Annotation[] annotations) {
        DocDescription docDescription = getAnnotation(annotations, DocDescription.class);
        if (docDescription == null) {
            return null;
        } else {
            return docDescription.value();
        }
    }

    org.projectodd.jrapidoc.model.Method createMethod(Method method, Class<?> seiClass) {
        Logger.debug("{0} method processing started", method.toString());
        org.projectodd.jrapidoc.model.Method.MethodBuilder methodBuilder = new org.projectodd.jrapidoc.model.Method.MethodBuilder();
        methodBuilder.description(getDescription(method.getDeclaredAnnotations())).isAsynchronous(true);
        addOperationName(method, methodBuilder);
        addInputHeaders(method, methodBuilder);
        addInputParams(method, methodBuilder);
        addReturn(method, methodBuilder);
        addSoapBinding(method, seiClass, methodBuilder);
        org.projectodd.jrapidoc.model.Method jrdMethod = methodBuilder.build();
        Logger.debug("{0} method processing finished", method.toString());
        return jrdMethod;
    }

    void addServiceName(Class<?> seiClass, Service.ResourceBuilder resourceBuilder) {
        WebService webServiceAnno = getAnnotation(seiClass.getDeclaredAnnotations(), WebService.class);
        String serviceName = seiClass.getSimpleName() + "Service";
        if (webServiceAnno != null) {
            if (StringUtils.isNotEmpty(webServiceAnno.serviceName())) {
                serviceName = webServiceAnno.serviceName();
            }
        }
        resourceBuilder.name(serviceName);
    }

    void addOperationName(Method method, org.projectodd.jrapidoc.model.Method.MethodBuilder methodBuilder) {
        WebMethod webMethodAnno = getAnnotation(method.getDeclaredAnnotations(), WebMethod.class);
        String operationName = method.getName();
        if (webMethodAnno != null) {
            if (StringUtils.isNotEmpty(webMethodAnno.operationName())) {
                operationName = webMethodAnno.operationName();
            }
        }
        methodBuilder.name(operationName);
    }

    void addSoapBinding(Method method, Class<?> seiClass, org.projectodd.jrapidoc.model.Method.MethodBuilder methodBuilder) {
        SOAPBinding soapBindingAnno = getAnnotation(method.getDeclaredAnnotations(), SOAPBinding.class);
        SoapBinding.SoapBindingBuilder soapBindingBuilder = new SoapBinding.SoapBindingBuilder();
        if (soapBindingAnno == null) {
            soapBindingAnno = getAnnotation(seiClass.getDeclaredAnnotations(), SOAPBinding.class);
        }
        if (soapBindingAnno != null) {
            soapBindingBuilder.parameterStyle(soapBindingAnno.parameterStyle().name()).style(soapBindingAnno.style().name()).use(soapBindingAnno.use().name()).build();
        }
        methodBuilder.soapBinding(soapBindingBuilder.build());
    }

    void addReturn(Method method, org.projectodd.jrapidoc.model.Method.MethodBuilder methodBuilder) {
        if (!isOneWay(method)) {
            Return.ReturnBuilder returnBuilder = new Return.ReturnBuilder();
            addReturnOptionDescription(method, returnBuilder);
            addOutputHeaders(method, returnBuilder);
            addOutputParams(method, returnBuilder);
            HeaderParam headerParam = new HeaderParam.HeaderParamBuilder().setName(HeaderParam.CONTENT_TYPE).setOptions(new String[]{"application/xml"}).setRequired(true).build();
            returnBuilder.httpStatus(200).headerParams(Arrays.asList(headerParam));
            Return returnOption = returnBuilder.build();
            List<Return> returnOptions = new ArrayList<Return>(Arrays.asList(new Return[]{returnOption}));
            addExceptionTypes(method, returnOptions);
            methodBuilder.returnOptions(returnOptions);
        }
    }

    /**
     * Used for non exception return option
     *
     * @param method
     * @param returnBuilder
     */
    private void addReturnOptionDescription(Method method, Return.ReturnBuilder returnBuilder) {
        DocReturn returnAnno = getNonExceptionDocReturn(method);
        String returnOptionDesc = (returnAnno == null) ? null : returnAnno.description();
        returnBuilder.description(StringUtils.isEmpty(returnOptionDesc) ? null : returnAnno.description());
    }

    private DocReturn getNonExceptionDocReturn(Method method) {
        DocReturn returnAnno = method.getAnnotation(DocReturn.class);
        DocReturns returnsAnno = method.getAnnotation(DocReturns.class);
        if (returnsAnno != null) {
            for (DocReturn docReturn : returnsAnno.value()) {
                if (docReturn.type().equals(Void.class)) {
                    returnAnno = docReturn;
                    break;
                }
            }
        }
        return returnAnno;
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

    Type extractFromHolder(Type holder) {
        if (holder instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) holder;
            if (paramType.getRawType().equals(Holder.class)) {
                return paramType.getActualTypeArguments()[0];
            }
        }
        return holder;
    }

    void addInputHeaders(Method method, org.projectodd.jrapidoc.model.Method.MethodBuilder methodBuilder) {
        for (int i = 0; i < method.getGenericParameterTypes().length; i++) {
            Type param = extractFromHolder(method.getGenericParameterTypes()[i]);
            Annotation[] annotations = method.getParameterAnnotations()[i];
            if (isHeader(annotations)) {
                if (isInputMode(annotations)) {
                    TransportType soapInputHeader = new TransportType.TransportTypeBuilder().type(createType(param)).description(getDescription(annotations)).isRequired(getIsRequired(annotations)).build();
                    methodBuilder.soapInputHeader(soapInputHeader);
                }
            }
        }
    }

    void addInputParams(Method method, org.projectodd.jrapidoc.model.Method.MethodBuilder methodBuilder) {
        List<org.projectodd.jrapidoc.model.object.type.Type> parameterTypes = new ArrayList<org.projectodd.jrapidoc.model.object.type.Type>();
        for (int i = 0; i < method.getGenericParameterTypes().length; i++) {
            Type param = extractFromHolder(method.getGenericParameterTypes()[i]);
            org.projectodd.jrapidoc.model.object.type.Type parameterType = createType(param);
            Annotation[] annotations = method.getParameterAnnotations()[i];
            if (isInputMode(annotations)) {
                if (!isHeader(annotations)) {
                    TransportType soapInputParameter = new TransportType.TransportTypeBuilder().type(createType(param)).description(getDescription(annotations)).isRequired(getIsRequired(annotations)).build();
                    methodBuilder.parameter(soapInputParameter);
                }
            }
        }
    }

    void addOutputHeaders(Method method, Return.ReturnBuilder returnBuilder) {
        if (!isOneWay(method)) {
            for (int i = 0; i < method.getGenericParameterTypes().length; i++) {
                Type param = extractFromHolder(method.getGenericParameterTypes()[i]);
                Annotation[] annotations = method.getParameterAnnotations()[i];
                if (isHeader(annotations)) {
                    if (isOutputMode(annotations)) {
                        TransportType soapOutputHeader = new TransportType.TransportTypeBuilder().type(createType(param)).description(getDescription(annotations)).build();
                        returnBuilder.soapOutputHeader(soapOutputHeader);
                    }
                }
            }
            addHeaderFromReturn(method, returnBuilder);
        }
    }

    void addOutputParams(Method method, Return.ReturnBuilder returnBuilder) {
        if (!isOneWay(method)) {
            List<TransportType> returnTypes = new ArrayList<TransportType>();
            for (int i = 0; i < method.getGenericParameterTypes().length; i++) {
                Type param = extractFromHolder(method.getGenericParameterTypes()[i]);
                Annotation[] annotations = method.getParameterAnnotations()[i];
                TransportType soapOutputParameter = new TransportType.TransportTypeBuilder().type(createType(param)).description(getDescription(annotations)).build();
                if (isOutputMode(method.getParameterAnnotations()[i])) {
                    if (!isHeader(method.getParameterAnnotations()[i])) {
                        returnTypes.add(soapOutputParameter);
                    }
                }
            }
            addTypeFromReturn(method, returnTypes);
            returnBuilder.returnTypes(returnTypes);
        }
    }

    void addExceptionTypes(Method method, List<Return> returnOptions) {
        DocReturns docReturnsAnno = method.getAnnotation(DocReturns.class);
        if (docReturnsAnno != null) {//take exception types from DocReturns annotation (precedence)
            for (DocReturn docReturnAnno : docReturnsAnno.value()) {
                if (!docReturnAnno.type().equals(Void.class)) {
                    String returnOptionDesc = StringUtils.isEmpty(docReturnAnno.description()) ? null : docReturnAnno.description();
                    String typeDescription = StringUtils.isEmpty(docReturnAnno.typeDescription()) ? null : docReturnAnno.typeDescription();
                    addExceptionType(returnOptionDesc, typeDescription, docReturnAnno.http(), docReturnAnno.type(), returnOptions);
                }
            }
        } else {
            //take exception types from method signature
            for (Class<?> exception : method.getExceptionTypes()) {
                addExceptionType(null, null, 500, exception, returnOptions);
            }
        }
    }

    void addExceptionType(String returnOptionDesc, String typeDescription, int httpStatus, Class<?> classType, List<Return> returnOptions) {
        TransportType exceptionTransport = new TransportType.TransportTypeBuilder().type(createType(classType)).description(typeDescription).build();
        List<TransportType> transportTypes = new ArrayList<TransportType>(Arrays.asList(new TransportType[]{exceptionTransport}));
        List<HeaderParam> httpHeaders = new ArrayList<HeaderParam>(Arrays.asList(new HeaderParam[]{new HeaderParam.HeaderParamBuilder().setName(HeaderParam.CONTENT_TYPE).setOptions(new String[]{"application/xml"}).setRequired(true).build()}));
        Return returnException = new Return.ReturnBuilder().httpStatus(httpStatus).description(returnOptionDesc).returnTypes(transportTypes).headerParams(httpHeaders).build();
        returnOptions.add(returnException);
    }

    void addTypeFromReturn(Method method, List<TransportType> returnTypes) {
        if (!isHeader(method.getDeclaredAnnotations())) {
            if (!method.getGenericReturnType().equals(Void.TYPE)) {
                DocReturn docReturnAnno = getNonExceptionDocReturn(method);
                String description = (docReturnAnno == null) ? null : docReturnAnno.typeDescription();
                TransportType soapOutputParameter = new TransportType.TransportTypeBuilder().description(description).type(createType(method.getGenericReturnType())).build();
                returnTypes.add(soapOutputParameter);
            }
        }
    }

    void addHeaderFromReturn(Method method, Return.ReturnBuilder returnBuilder) {
        if (isHeader(method.getDeclaredAnnotations())) {
            if (!method.getGenericReturnType().equals(Void.TYPE)) {
                DocReturn docReturnAnno = getNonExceptionDocReturn(method);
                String typeDescription = (docReturnAnno == null) ? null : docReturnAnno.typeDescription();
                TransportType soapOutputHeader = new TransportType.TransportTypeBuilder().description(typeDescription).type(createType(method.getGenericReturnType())).build();
                returnBuilder.soapOutputHeader(soapOutputHeader);
            }
        }
    }

    boolean isHeader(Annotation[] annotations) {
        WebParam webParamAnno = getAnnotation(annotations, WebParam.class);
        WebResult webResultAnno = getAnnotation(annotations, WebResult.class);
        if (webParamAnno == null && webResultAnno == null) {
            return false;
        }
        if (webParamAnno != null) {
            return webParamAnno.header();
        } else if (webResultAnno != null) {
            return webResultAnno.header();
        }
        return false;
    }

    boolean isInputMode(Annotation[] annotations) {
        if (annotations == null) {
            return true;
        }
        for (Annotation a : annotations) {
            if (a.annotationType().equals(WebParam.class)) {
                return ((WebParam) a).mode() != WebParam.Mode.OUT;
            }
        }
        return true;
    }

    boolean isOutputMode(Annotation[] annotations) {
        if (annotations == null) {
            return false;
        }
        for (Annotation a : annotations) {
            if (a.annotationType().equals(WebParam.class)) {
                return ((WebParam) a).mode() != WebParam.Mode.IN;
            }
        }
        return false;
    }

    boolean isOneWay(Method method) {
        return (method.getAnnotation(Oneway.class) != null);
    }

    void addMethodName(Method method, org.projectodd.jrapidoc.model.Method.MethodBuilder methodBuilder) {
        WebMethod webMetAnno = method.getAnnotation(WebMethod.class);
        if (webMetAnno == null) {
            methodBuilder.name(method.getName());
            return;
        } else if (StringUtils.isNotEmpty(webMetAnno.operationName())) {
            methodBuilder.name(webMetAnno.operationName());
            return;
        }
        methodBuilder.name(method.getName());
    }

    Boolean getIsRequired(Annotation[] annotations){
        DocIsRequired isRequired = getAnnotation(annotations, DocIsRequired.class);
        if(isRequired == null){
            return null;
        }
        return isRequired.value();
    }
}
