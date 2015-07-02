package org.projectodd.jrapidoc.model.type;

import com.fasterxml.jackson.databind.JavaType;
import org.projectodd.jrapidoc.model.object.type.Type;
import org.projectodd.jrapidoc.model.type.provider.converter.JacksonToJrapidocProcessor;
import org.projectodd.jrapidoc.model.type.provider.TypeProvider;
import org.projectodd.jrapidoc.model.type.provider.TypeProviderFactory;
import org.junit.Test;

import java.util.Map;

/**
 * Created by Tomas "sarzwest" Jiricek on 29.3.15.
 */
public class TestTypeProvider {

    @Test
    public void responseObjectProcessor(){
        JavaType javaType = JacksonUtil.objectMapperInstance().getTypeFactory().constructParametrizedType(Map.class, Map.class, Integer.class, String.class);
        TypeProvider typeProvider = TypeProviderFactory.createTypeProvider(null, null);

        Type type = typeProvider.createType(String.class);
        for (String key : JacksonToJrapidocProcessor.cache.keySet()) {
            System.out.println(key);
            System.out.println(JacksonToJrapidocProcessor.cache.get(key));
            System.out.println();
        }
    }
}
