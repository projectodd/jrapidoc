package org.projectodd.jrapidoc.model.handler;

import org.projectodd.jrapidoc.exception.JrapidocExecutionException;
import org.projectodd.jrapidoc.logger.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomas "sarzwest" Jiricek on 12.4.15.
 */
public class HandlerFactory {

    public static List<ModelHandler> createModelHandlers(List<String> handlerClassesToLoad, ClassLoader loader) throws JrapidocExecutionException {
        try {
            List<ModelHandler> handlers = new ArrayList<ModelHandler>();
            if (handlerClassesToLoad == null || handlerClassesToLoad.isEmpty()) {
                Logger.debug("No model handler classes found");
                return handlers;
            }
            for (String clazzToLoad : handlerClassesToLoad) {
                Logger.info("Loading class {0}", clazzToLoad);
//                Class<?> handler = Thread.currentThread().getContextClassLoader().loadClass(clazzToLoad);
                Class<?> handler = loader.loadClass(clazzToLoad);
                Logger.debug("Creating new instance from class {0} as ModelHandler", clazzToLoad);
                handlers.add((ModelHandler) handler.newInstance());
            }
            return handlers;
        }catch (ClassNotFoundException e){
            Logger.error(e, "Class was not found");
            throw new JrapidocExecutionException(e.getMessage(), e);
        } catch (InstantiationException e) {
            Logger.error(e.getMessage());
            throw new JrapidocExecutionException(e.getMessage(), e);
        } catch (IllegalAccessException e) {
            Logger.error(e.getMessage());
            throw new JrapidocExecutionException(e.getMessage(), e);
        }
    }
}
