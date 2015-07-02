package org.projectodd.jrapidoc.introspector;

import org.apache.commons.lang3.StringUtils;
import org.projectodd.jrapidoc.exception.JrapidocExecutionException;
import org.projectodd.jrapidoc.exception.JrapidocFailureException;
import org.projectodd.jrapidoc.logger.Logger;
import org.projectodd.jrapidoc.model.APIModel;
import org.projectodd.jrapidoc.model.ServiceGroup;
import org.projectodd.jrapidoc.model.handler.ModelHandler;
import org.projectodd.jrapidoc.model.type.provider.JacksonJaxbJsonProvider;
import org.projectodd.jrapidoc.model.type.provider.JacksonJaxbProvider;
import org.projectodd.jrapidoc.model.type.provider.TypeProvider;
import org.projectodd.jrapidoc.model.type.provider.TypeProviderFactory;
import org.projectodd.jrapidoc.plugin.ConfigGroup;

import javax.jws.WebService;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Tomas "sarzwest" Jiricek on 7.4.15.
 */
public class SoapIntrospector extends AbstractIntrospector {

    @Override
    public void run(URL[] urlsForClassloader, List<ConfigGroup> groups, String typeProviderClass, File output, List<String> modelHandlerClasses, Map<String, String> customInfo) throws JrapidocExecutionException, JrapidocFailureException {
        Logger.info("");
        Logger.info("Introspection started");
        Logger.info("");
        setUp(groups, output);
        URLClassLoader loader = getProjectUrlClassLoader(urlsForClassloader);
        List<ModelHandler> modelHandlers = getModelHandlers(modelHandlerClasses, loader);
        APIModel apiModel = createModel(customInfo, groups, loader, typeProviderClass);
        processHandlers(modelHandlers, apiModel);
        writeModelToFile(apiModel, output);
        Logger.info("");
        Logger.info("Introspection finished");
        Logger.info("");
    }

    APIModel createModel(Map<String, String> customInfo, List<ConfigGroup> groups, ClassLoader loader, String typeProviderClass) throws JrapidocExecutionException, JrapidocFailureException {
        try {
            TypeProvider typeProvider = getTypeProvider(typeProviderClass, loader);
            SEIProcessor seiProcessor = getSeiClassProcessor(typeProvider, loader);
            APIModel.APIModelBuilder APIModelBuilder = new APIModel.APIModelBuilder();
            addCustomInfo(customInfo, APIModelBuilder);
            addServiceGroups(groups, seiProcessor, loader, APIModelBuilder);
            APIModelBuilder.types(typeProvider.getUsedTypes());
            return APIModelBuilder.build();
        } catch (Exception e) {
            Logger.error(e, "Unexpected error during creating model");
            throw new JrapidocFailureException(e.getMessage(), e);
        }
    }

    TypeProvider getTypeProvider(String typeProviderClass, ClassLoader loader) {
        return TypeProviderFactory.createTypeProvider((StringUtils.isEmpty(typeProviderClass) ? JacksonJaxbProvider.class.getCanonicalName() : typeProviderClass), loader);
    }

    ServiceGroup createServiceGroup(String basePath, String description, Set<Class<?>> resourceClasses, SEIProcessor seiProcessor) throws JrapidocExecutionException {
        ServiceGroup.ServiceGroupBuilder serviceGroupBuilder = new ServiceGroup.ServiceGroupBuilder();
        serviceGroupBuilder.baseUrl(basePath);
        serviceGroupBuilder.description(description);
        return seiProcessor.createServiceGroup(resourceClasses, serviceGroupBuilder);
    }

    void addServiceGroups(List<ConfigGroup> groups, SEIProcessor seiProcessor, ClassLoader loader, APIModel.APIModelBuilder APIModelBuilder) throws JrapidocExecutionException {
        for (ConfigGroup group : groups) {
            Logger.info("Service group {0} processing started", group.getBaseUrl());
            Set<Class<?>> resourceClasses = getScannedClasses(group.getIncludes(), group.getExcludes(), loader, WebService.class);
            resourceClasses = removeInterfaces(resourceClasses);
            ServiceGroup serviceGroup = createServiceGroup(group.getBaseUrl(), group.getDescription(), resourceClasses, seiProcessor);
            APIModelBuilder.resourceGroup(serviceGroup);
            Logger.info("Service group {0} processing finished", group.getBaseUrl());
        }
    }

    SEIProcessor getSeiClassProcessor(TypeProvider typeProvider, ClassLoader loader) {
        return new SEIProcessor(typeProvider, loader);
    }

    Set<Class<?>> removeInterfaces(Set<Class<?>> seiClasses) {
        Set<Class<?>> classes = new HashSet<Class<?>>();
        for (Class<?> clazz : seiClasses) {
            if (!clazz.isInterface()) {
                classes.add(clazz);
            }
        }
        return classes;
    }
}
