package org.projectodd.jrapidoc.model.type.provider;

import org.apache.commons.lang3.StringUtils;
import org.projectodd.jrapidoc.logger.Logger;

/**
 * Created by Tomas "sarzwest" Jiricek on 25.3.15.
 */
public class TypeProviderFactory {

    public static TypeProvider createTypeProvider(String clazzToLoad, ClassLoader loader) {
        try {
            if (StringUtils.isNotEmpty(clazzToLoad)) {
                Logger.info("Loading class {0}", clazzToLoad);
//                Class<?> providerImpl = Thread.currentThread().getContextClassLoader().loadClass(clazzToLoad);
                Class<?> providerImpl = loader.loadClass(clazzToLoad);
                Logger.debug("Creating new instance from class {0} as TypeProvider", clazzToLoad);
                return (TypeProvider)providerImpl.newInstance();
            }else{
                Logger.info("Using default TypeProvider instance");
                return new JacksonJsonProvider();
            }
        } catch (Exception e) {
            Logger.warn(e, "Exception occured during loading {0} as TypeProvider, using default TypeProvider", clazzToLoad);
            return new JacksonJsonProvider();
        }
    }
}
