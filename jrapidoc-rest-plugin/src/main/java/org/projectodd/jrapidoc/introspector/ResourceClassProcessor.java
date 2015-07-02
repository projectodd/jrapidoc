package org.projectodd.jrapidoc.introspector;

import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.spi.metadata.*;
import org.projectodd.jrapidoc.RestUtil;
import org.projectodd.jrapidoc.logger.Logger;
import org.projectodd.jrapidoc.model.*;
import org.projectodd.jrapidoc.model.object.type.Type;
import org.projectodd.jrapidoc.model.param.*;
import org.projectodd.jrapidoc.model.type.provider.TypeProvider;

import java.util.*;

/**
 * Created by Tomas "sarzwest" Jiricek on 26.1.15.
 */
public class ResourceClassProcessor {

    TypeProvider typeProvider;

    public ResourceClassProcessor(TypeProvider typeProvider) {
        this.typeProvider = typeProvider;
    }

    /**
     * Main method, creates API model from metadata
     *
     * @param resourceClasses
     * @return
     */
    public ServiceGroup createServiceGroup(Set<ResourceClass> resourceClasses, ServiceGroup.ServiceGroupBuilder serviceGroupBuilder) {
        for (ResourceClass resourceClass : resourceClasses) {
            Logger.info("{0} processing started", resourceClass.getClazz().getCanonicalName());
            Service service = createResource(resourceClass);
            serviceGroupBuilder.service(service);
            Logger.info("{0} processing finished", resourceClass.getClazz().getCanonicalName());
        }
        return serviceGroupBuilder.build();
    }

    void addConsumesParam(Method.MethodBuilder methodBuilder, ResourceClass resourceClass, ResourceMethod resourceMethod) {
        if (resourceMethod.getConsumes() != null) {
            Param param = new HeaderParam.HeaderParamBuilder().setOptions(resourceMethod.getConsumes()).setName(HeaderParam.ACCEPT).build();
            methodBuilder.param(param.getType(), param);
        }
        if (resourceClass.getConsumes() != null) {
            Param param = new HeaderParam.HeaderParamBuilder().setOptions(resourceClass.getConsumes()).setName(HeaderParam.ACCEPT).build();
            methodBuilder.param(param.getType(), param);
        } else {
            Param param = new HeaderParam.HeaderParamBuilder().setOptions(new String[]{"*/*"}).setName(HeaderParam.ACCEPT).build();
            methodBuilder.param(param.getType(), param);
        }
    }

    void addProducesParam(Method.MethodBuilder methodBuilder, ResourceClass resourceClass, ResourceMethod resourceMethod) {
        if (resourceMethod.getProduces() != null) {
            Param param = new HeaderParam.HeaderParamBuilder().setOptions(resourceMethod.getProduces()).setName(HeaderParam.CONTENT_TYPE).build();
            methodBuilder.param(param.getType(), param);
        }
        if (resourceClass.getProduces() != null) {
            Param param = new HeaderParam.HeaderParamBuilder().setOptions(resourceClass.getProduces()).setName(HeaderParam.CONTENT_TYPE).build();
            methodBuilder.param(param.getType(), param);
        } else {
            Param param = new HeaderParam.HeaderParamBuilder().setOptions(new String[]{"*/*"}).setName(HeaderParam.CONTENT_TYPE).build();
            methodBuilder.param(param.getType(), param);
        }
    }

    Service createResource(ResourceClass resourceClass) {
        Service.ResourceBuilder resourceBuilder = new Service.ResourceBuilder();
        addPath(resourceClass, resourceBuilder);
        addPathExample(resourceClass, resourceBuilder);
        resourceBuilder.description(resourceClass.getDescription());
        addMethods(resourceClass, resourceBuilder);
        addLocatorMethods(resourceClass, resourceBuilder);
        return resourceBuilder.build();
    }

    void addPathExample(ResourceClass resourceClass, Service.ResourceBuilder resourceBuilder) {
        if (StringUtils.isEmpty(resourceClass.getPathExample())) {
            resourceBuilder.pathExample(null);
        } else {
            String pathExample = RestUtil.getPathInModelFormat(resourceClass.getPathExample());
            resourceBuilder.pathExample(pathExample);
        }

    }

    void addPath(ResourceClass resourceClass, Service.ResourceBuilder resourceBuilder) {
        String path = RestUtil.getPathInModelFormat(resourceClass.getPath());
        resourceBuilder.path(path);
    }

    void addLocatorMethods(ResourceClass resourceClass, Service.ResourceBuilder resourceBuilder) {
        for (ResourceLocator resourceLocator : resourceClass.getResourceLocators()) {
            try {
                Logger.info("{0} subresource locator processing started", resourceLocator.getReturnType().getCanonicalName());
                ResourceClass newResourceClass = ResourceBuilder.locatorFromAnnotations(resourceLocator.getReturnType());
                newResourceClass.setPath(resourceLocator.getFullpath());
                newResourceClass.setConstructor(resourceClass.getConstructor());
                ServiceGroup.ServiceGroupBuilder serviceGroupBuilder = new ServiceGroup.ServiceGroupBuilder();
                ServiceGroup locatorSubModel = createServiceGroup(new HashSet<ResourceClass>(Arrays.asList(new ResourceClass[]{newResourceClass})), serviceGroupBuilder);
                for (Service service : locatorSubModel.getServices().values()) {
                    for (Method method : service.getMethods().values()) {
                        resourceBuilder.method(method);
                    }
                }
            } catch (Exception e) {
                Logger.error(e, "Problem during preintrospection of locator class {0}, skipping this class", resourceLocator.getReturnType().getCanonicalName());
            } finally {
                Logger.info("{0} subresource locator processing finished", resourceLocator.getReturnType().getCanonicalName());
            }
        }
    }

    void addMethods(ResourceClass resourceClass, Service.ResourceBuilder resourceBuilder) {
        for (ResourceMethod resourceMethod : resourceClass.getResourceMethods()) {
            Method method = createMethod(resourceMethod, resourceClass);
            resourceBuilder.method(method);
            for (String httpMethod : resourceMethod.getHttpMethods()) {
                method = method.clone(httpMethod);
                resourceBuilder.method(method);
            }
        }
    }

    Method createMethod(ResourceMethod resourceMethod, ResourceClass resourceClass) {
        Logger.debug("{0} method processing started", resourceMethod.getMethod().toString());
        resourceMethod.messageBodyCheck();
        Method.MethodBuilder methodBuilder = new Method.MethodBuilder();
        methodBuilder.isAsynchronous(resourceMethod.isAsynchronous());
        methodBuilder.description(resourceMethod.getDescription());
        addClassParams(methodBuilder, resourceClass);
        addConsumesParam(methodBuilder, resourceClass, resourceMethod);
        addProducesParam(methodBuilder, resourceClass, resourceMethod);
        addMethodParams(methodBuilder, resourceMethod);
        addPaths(methodBuilder, resourceClass, resourceMethod);
        methodBuilder.parameter(createParameterType(resourceMethod.getParams()));
        removeThisMethodFromResource(resourceMethod, methodBuilder);
        methodBuilder.returnOptions(createReturnOptions(resourceMethod));
        Method method = methodBuilder.build();
        Logger.debug("{0} method processing finished", resourceMethod.getMethod().toString());
        return method;
    }

    void removeThisMethodFromResource(ResourceMethod resourceMethod, Method.MethodBuilder methodBuilder) {
        for (String httpMethod : resourceMethod.getHttpMethods()) {
            methodBuilder.httpMethodType(httpMethod);
            resourceMethod.getHttpMethods().remove(httpMethod);
            break;
        }
    }

    List<Return> createReturnOptions(ResourceMethod resourceMethod) {
        List<Return> returnObjects = new ArrayList<Return>();
        for (ReturnOption returnOption : resourceMethod.getReturnOptions()) {
            returnObjects.add(createReturnOption(returnOption));
        }
        return returnObjects;
    }

    Return createReturnOption(ReturnOption returnOption) {
        List<TransportType> returnTypes = new ArrayList<TransportType>();
        if (returnOption.getReturnClass() != null &&
                !returnOption.getReturnClass().equals(Void.class)) {
            Type returnType = typeProvider.createType(returnOption.getParameterized());
            returnTypes.add(new TransportType.TransportTypeBuilder().type(returnType).description(StringUtils.isEmpty(returnOption.getTypeDescription()) ? null : returnOption.getTypeDescription()).build());
        }
        List<HeaderParam> headerParams = createReturnHeaders(returnOption.getHeaders());
        List<CookieParam> cookieParams = createReturnCookies(returnOption.getCookies());
        return new Return.ReturnBuilder().httpStatus(returnOption.getStatus()).headerParams(headerParams).cookieParams(cookieParams).returnTypes(returnTypes).description(returnOption.getDescription()).build();
    }

    List<HeaderParam> createReturnHeaders(List<String> headersString) {
        List<HeaderParam> items = new ArrayList<HeaderParam>();
        for (String header : headersString) {
            items.add(new HeaderParam.HeaderParamBuilder().setName(header).setTypeRef(ModelUtil.getSimpleTypeSignature(String.class, null)).build());
        }
        return items;
    }

    List<CookieParam> createReturnCookies(List<String> cookiesString) {
        List<CookieParam> items = new ArrayList<CookieParam>();
        for (String header : cookiesString) {
            items.add(new CookieParam.CookieParamBuilder().setName(header).setTypeRef(ModelUtil.getSimpleTypeSignature(String.class, null)).build());
        }
        return items;
    }

    void addPaths(Method.MethodBuilder methodBuilder, ResourceClass resourceClass, ResourceMethod resourceMethod) {
        String methodPath = RestUtil.getPathInModelFormat(RestUtil.trimSlash(resourceClass.getPath()) + "/" + RestUtil.trimSlash(resourceMethod.getPath()));
        methodBuilder.path(methodPath);
        String methodPathExample;
        if(StringUtils.isEmpty(resourceClass.getPathExample())){
            methodPathExample = RestUtil.trimSlash(resourceClass.getPath());
        }else{
            methodPathExample = RestUtil.trimSlash(resourceClass.getPathExample());
        }
        methodPathExample += "/";
        if(StringUtils.isEmpty(resourceMethod.getPathExample())){
            methodPathExample += RestUtil.trimSlash(resourceMethod.getPath());
        }else{
            methodPathExample += RestUtil.trimSlash(resourceMethod.getPathExample());
        }
        methodPathExample = RestUtil.getPathInModelFormat(methodPathExample);
        if(methodPathExample.equals(methodPath)){
            methodBuilder.pathExample(null);
        }else{
            methodBuilder.pathExample(methodPathExample);
        }
    }

    void addMethodParams(Method.MethodBuilder methodBuilder, ResourceMethod resourceMethod) {
        for (MethodParameter methodParameter : resourceMethod.getParams()) {
            if (RestUtil.isHttpParam(methodParameter)) {
                Param param = createParam(methodParameter);
                methodBuilder.param(param.getType(), param);
            }
        }
    }

    void addClassParams(Method.MethodBuilder methodBuilder, ResourceClass resourceClass) {
        List<Parameter> classParams = new ArrayList<Parameter>();
        Collections.addAll(classParams, resourceClass.getFields());
        Collections.addAll(classParams, resourceClass.getConstructor().getParams());
        Collections.addAll(classParams, resourceClass.getSetters());
        for (Parameter parameter : classParams) {
            if (RestUtil.isHttpParam(parameter)) {
                Param param = createParam(parameter);
                methodBuilder.param(param.getType(), param);
            }
        }
    }

    Param createParam(Parameter parameter) {
        Param.ParamBuilder paramBuilder = null;
        if (parameter.getParamType().name().equals(Param.Type.QUERY_PARAM.name())) {
            paramBuilder = new QueryParam.QueryParamBuilder();
        } else if (parameter.getParamType().name().equals(Parameter.ParamType.HEADER_PARAM.name())) {
            paramBuilder = new HeaderParam.HeaderParamBuilder();
        } else if (parameter.getParamType().name().equals(Param.Type.PATH_PARAM.name())) {
            paramBuilder = new PathParam.PathParamBuilder();
        } else if (parameter.getParamType().name().equals(Param.Type.MATRIX_PARAM.name())) {
            paramBuilder = new MatrixParam.MatrixParamBuilder();
        } else if (parameter.getParamType().name().equals(Param.Type.COOKIE_PARAM.name())) {
            paramBuilder = new CookieParam.CookieParamBuilder();
        } else if (parameter.getParamType().name().equals(Param.Type.FORM_PARAM.name())) {
            paramBuilder = new FormParam.FormParamBuilder();
        }
        paramBuilder.setName(parameter.getParamName());
        Type type = createParameterType(parameter);
        paramBuilder.setTypeRef(type.getTypeRef());
        paramBuilder.setDescription(parameter.getDescription());
        paramBuilder.setRequired(parameter.getIsRequired());
        return paramBuilder.build();
    }

    /**
     * Dosazeni za *Param typy a vytvoreni spravneho typu
     */
    private Type createParameterType(Parameter parameter) {
        if (ModelUtil.isNumericType(parameter.getType())) {
            return typeProvider.createType(Integer.class);
        }
        if (ModelUtil.isBooleanType(parameter.getType())) {
            return typeProvider.createType(Boolean.class);
        }
        return typeProvider.createType(String.class);
    }

    private TransportType createParameterType(MethodParameter[] parameters) {
        for (MethodParameter methodParameter : parameters) {
            if (methodParameter.getParamType().equals(MethodParameter.ParamType.MESSAGE_BODY)) {
                return new TransportType.TransportTypeBuilder().type(typeProvider.createType(methodParameter.getGenericType())).description(methodParameter.getDescription()).build();
            }
        }
        return null;
    }
}
