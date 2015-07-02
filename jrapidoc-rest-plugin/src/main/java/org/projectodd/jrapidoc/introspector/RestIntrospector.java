package org.projectodd.jrapidoc.introspector;

import org.apache.commons.lang3.StringUtils;
import org.jboss.resteasy.spi.metadata.ResourceBuilder;
import org.jboss.resteasy.spi.metadata.ResourceClass;
import org.projectodd.jrapidoc.exception.JrapidocExecutionException;
import org.projectodd.jrapidoc.exception.JrapidocFailureException;
import org.projectodd.jrapidoc.logger.Logger;
import org.projectodd.jrapidoc.model.APIModel;
import org.projectodd.jrapidoc.model.ServiceGroup;
import org.projectodd.jrapidoc.model.handler.ModelHandler;
import org.projectodd.jrapidoc.model.type.provider.JacksonJaxbProvider;
import org.projectodd.jrapidoc.model.type.provider.JacksonJsonProvider;
import org.projectodd.jrapidoc.model.type.provider.TypeProvider;
import org.projectodd.jrapidoc.model.type.provider.TypeProviderFactory;
import org.projectodd.jrapidoc.plugin.ConfigGroup;

import javax.ws.rs.Path;
import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Tomas "sarzwest" Jiricek on 23.3.15.
 */
public class RestIntrospector extends AbstractIntrospector {

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

    ServiceGroup createServiceGroup(String basePath, String description, Set<Class<?>> resourceClasses, ResourceClassProcessor resourceClassProcessor) {
        ServiceGroup.ServiceGroupBuilder serviceGroupBuilder = new ServiceGroup.ServiceGroupBuilder();
        serviceGroupBuilder.baseUrl(basePath);
        serviceGroupBuilder.description(description);
        Set<ResourceClass> resourceClassesMeta = doPreIntrospection(resourceClasses);
        return resourceClassProcessor.createServiceGroup(resourceClassesMeta, serviceGroupBuilder);
    }

    APIModel createModel(Map<String, String> customInfo, List<ConfigGroup> groups, ClassLoader loader, String typeProviderClass) throws JrapidocFailureException {
        try {
            TypeProvider typeProvider = getTypeProvider(typeProviderClass, loader);
            ResourceClassProcessor resourceClassProcessor = getResourceClassProcessor(typeProvider);
            APIModel.APIModelBuilder APIModelBuilder = new APIModel.APIModelBuilder();
            addCustomInfo(customInfo, APIModelBuilder);
            addServiceGroups(groups, resourceClassProcessor, loader, APIModelBuilder);
            APIModelBuilder.types(typeProvider.getUsedTypes());
            return APIModelBuilder.build();
        }catch (Exception e){
            Logger.error(e, "Unexpected error during creating model");
            throw new JrapidocFailureException(e.getMessage(), e);
        }
    }

    TypeProvider getTypeProvider(String typeProviderClass, ClassLoader loader) {
        return TypeProviderFactory.createTypeProvider((StringUtils.isEmpty(typeProviderClass) ? JacksonJsonProvider.class.getCanonicalName() : typeProviderClass), loader);
    }

    void addServiceGroups(List<ConfigGroup> groups, ResourceClassProcessor resourceClassProcessor, ClassLoader loader, APIModel.APIModelBuilder APIModelBuilder) {
        for (ConfigGroup group:groups) {
            Logger.info("Service group {0} processing started", group.getBaseUrl());
            Set<Class<?>> resourceClasses = getScannedClasses(group.getIncludes(), group.getExcludes(), loader, Path.class);
            ServiceGroup serviceGroup = createServiceGroup(group.getBaseUrl(), group.getDescription(), resourceClasses, resourceClassProcessor);
            APIModelBuilder.resourceGroup(serviceGroup);
            Logger.info("Service group {0} processing finished", group.getBaseUrl());
        }
    }

    ResourceClassProcessor getResourceClassProcessor(TypeProvider typeProvider) {
        return new ResourceClassProcessor(typeProvider);
    }

    Set<ResourceClass> doPreIntrospection(Set<Class<?>> resourceClasses) {
        Set<ResourceClass> resourceClassesMeta = new HashSet<ResourceClass>();
        for (Class<?> rootResourceClass : resourceClasses) {
            try {
                Logger.info("{0} preprocessing started", rootResourceClass.getCanonicalName());
                ResourceClass resourceClass = ResourceBuilder.rootResourceFromAnnotations(rootResourceClass);
                resourceClassesMeta.add(resourceClass);
            }catch (Exception e){
                Logger.error(e, "Problem during preintrospection of class {0}, skipping this resource class", rootResourceClass.getCanonicalName());
            }finally {
                Logger.info("{0} preprocessing finished", rootResourceClass.getCanonicalName());
            }
        }
        return resourceClassesMeta;
    }
}
