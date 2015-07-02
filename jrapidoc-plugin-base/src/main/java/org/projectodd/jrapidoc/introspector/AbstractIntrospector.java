package org.projectodd.jrapidoc.introspector;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.projectodd.jrapidoc.exception.JrapidocExecutionException;
import org.projectodd.jrapidoc.exception.JrapidocFailureException;
import org.projectodd.jrapidoc.logger.Logger;
import org.projectodd.jrapidoc.model.APIModel;
import org.projectodd.jrapidoc.model.generator.ModelGenerator;
import org.projectodd.jrapidoc.model.handler.HandlerException;
import org.projectodd.jrapidoc.model.handler.HandlerFactory;
import org.projectodd.jrapidoc.model.handler.ModelHandler;
import org.projectodd.jrapidoc.model.type.provider.JacksonJaxbJsonProvider;
import org.projectodd.jrapidoc.model.type.provider.JacksonJaxbProvider;
import org.projectodd.jrapidoc.model.type.provider.TypeProvider;
import org.projectodd.jrapidoc.model.type.provider.TypeProviderFactory;
import org.projectodd.jrapidoc.plugin.ConfigGroup;
import org.reflections.Reflections;

import java.io.File;
import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Tomas "sarzwest" Jiricek on 12.4.15.
 */
public abstract class AbstractIntrospector {

    public abstract void run(URL[] urlsForClassloader, List<ConfigGroup> groups, String typeProviderClass, File output, List<String> modelHandlerClasses, Map<String, String> custom) throws JrapidocFailureException, JrapidocExecutionException;

    void writeModelToFile(APIModel apiModel, File output) throws JrapidocExecutionException {
        Logger.info("Writing model to file {0} started", output.getAbsolutePath());
        ModelGenerator.generateModel(apiModel, output);
        Logger.info("Writing model to file {0} finished", output.getAbsolutePath());
    }

    void processHandlers(List<ModelHandler> modelHandlers, APIModel apiModel) throws JrapidocFailureException, JrapidocExecutionException {
        if(modelHandlers == null || modelHandlers.isEmpty()){
            Logger.debug("No model handlers found");
            return;
        }
        for (ModelHandler modelHandler:modelHandlers){
            try {
                Logger.info("");
                Logger.info("{0} model handler instance started", modelHandler.getClass().getCanonicalName());
                Logger.info("");
                modelHandler.handleModel(apiModel);
            }catch (HandlerException e){
                if(e.getBehaviour() == HandlerException.Action.STOP_HANDLER_CURRENT){
                    Logger.info("Exception occurred in handler {0}, continue with processing next handler", modelHandler.getClass().getCanonicalName());
                }else if(e.getBehaviour() == HandlerException.Action.STOP_HANDLERS){
                    Logger.warn(e, "Exception occurred in handler {0}, skipping handlers processing", modelHandler.getClass().getCanonicalName());
                }else if (e.getBehaviour() == HandlerException.Action.FAILURE_EXCEPTION){
                    Logger.error(e, "Exception occurred in handler {0}, throwing {1}", modelHandler.getClass().getCanonicalName(), MojoFailureException.class.getCanonicalName());
                    throw new JrapidocFailureException(e.getMessage(), e);
                }else if(e.getBehaviour() == HandlerException.Action.EXECUTION_EXCEPTION){
                    Logger.error(e, "Exception occurred in handler {0}, throwing {1}", modelHandler.getClass().getCanonicalName(), MojoExecutionException.class.getCanonicalName());
                    throw new JrapidocExecutionException(e.getMessage(), e);
                }
            }finally {
                Logger.info("");
                Logger.info("{0} model handler instance finished", modelHandler.getClass().getCanonicalName());
                Logger.info("");
            }
        }
    }

    void addCustomInfo(Map<String, String> customInfo, APIModel.APIModelBuilder APIModelBuilder) {
        if (customInfo != null && !customInfo.isEmpty()) {
            for (String key : customInfo.keySet()) {
                APIModelBuilder.customInfo(key, customInfo.get(key));
            }
        }
    }

    List<ModelHandler> getModelHandlers(List<String> modelHandlerClasses, ClassLoader loader) throws JrapidocExecutionException {
        return HandlerFactory.createModelHandlers(modelHandlerClasses, loader);
    }

    void createOutputDir(File output) throws JrapidocExecutionException {
        if (!output.getParentFile().canWrite()) {
            if (!output.getParentFile().mkdirs()) {
                Logger.error("Directory {0} could not be created", output.getParentFile().getAbsolutePath());
                throw new JrapidocExecutionException("Directory could not be created");
            }
        }
    }

    Set<Class<?>> getScannedClasses(List<String> include, List<String> exclude, ClassLoader loader, Class<? extends Annotation> annotatedWith) {
        Reflections ref = getUnionOfIncludedPaths(include, loader);
        Set<Class<?>> resourceClassesAll = ref.getTypesAnnotatedWith(annotatedWith);
        Logger.debug("Service classes on path: {0}", resourceClassesAll.toString());
        return removeExcludedResourceClasses(exclude, resourceClassesAll);
    }

    URLClassLoader getProjectUrlClassLoader(URL[] urlsForClassloader) {
        return new URLClassLoader(urlsForClassloader,
                Thread.currentThread().getContextClassLoader());
    }

    Set<Class<?>> removeExcludedResourceClasses(List<String> exclude, Set<Class<?>> resourceClasses) {
        Set<Class<?>> resourceClassesFiltered = new HashSet<Class<?>>(resourceClasses);
        if(exclude == null || exclude.isEmpty()){
            return resourceClasses;
        }
        for (String excludePath : exclude) {
            for (Class<?> resourceClass : resourceClasses) {
                if (resourceClass.getCanonicalName().startsWith(excludePath)) {
                    Logger.debug("Removing class {0} from scan (it is in exclude property)", resourceClass.getCanonicalName());
                    resourceClassesFiltered.remove(resourceClass);
                }
            }
        }
        return resourceClassesFiltered;
    }

    Reflections getUnionOfIncludedPaths(List<String> include, ClassLoader loader) {
        Reflections ref;
        if (include == null || include.isEmpty()) {
            ref = new Reflections("", loader);
        } else {
            ref = new Reflections(include.remove(0), loader);
            for (String includePath : include) {
                Logger.debug(MessageFormat.format("Adding path {0} to scan", new Object[]{includePath}));
                ref.merge(new Reflections(includePath, loader));
            }
        }
        return ref;
    }

    void checkIncludeConfigNotEmpty(List<String> includes) throws JrapidocExecutionException {
        if(includes == null){
            Logger.error("\"includes\" element in configuration is null");
            throw new JrapidocExecutionException("\"includes\" element in configuration is null");
        }
        if(includes.isEmpty()){
            Logger.error("\"includes\" element in configuration is empty");
            throw new JrapidocExecutionException("\"includes\" element in configuration is empty");
        }
        for (String include:includes){
            if(StringUtils.isEmpty(include)){
                Logger.error("\"include\" element in \"includes\" element is empty");
                throw new JrapidocExecutionException("\"include\" element in \"includes\" element is empty");
            }
        }
    }

    void setUp(List<ConfigGroup> groups, File output) throws JrapidocExecutionException {
        createOutputDir(output);
        if(groups == null){
            Logger.error("\"groups\" element in configuration is null");
            throw new JrapidocExecutionException("\"groups\" element in configuration is null");
        }
        if(groups.isEmpty()){
            Logger.error("\"groups\" element in configuration is empty, nothing will be generated");
            throw new JrapidocExecutionException("\"groups\" element in configuration is empty, nothing will be generated");
        }
        for (ConfigGroup group:groups){
            if(group == null){
                Logger.error("\"group\" element in \"groups\" element is null");
                throw new JrapidocExecutionException("\"group\" element in \"groups\" element is null");
            }
            if(StringUtils.isEmpty(group.getBaseUrl())){
                Logger.error("\"baseUrl\" element in \"group\" element is null");
                throw new JrapidocExecutionException("\"baseUrl\" element in \"group\" element is null");
            }
            checkIncludeConfigNotEmpty(group.getIncludes());
        }
    }
}
