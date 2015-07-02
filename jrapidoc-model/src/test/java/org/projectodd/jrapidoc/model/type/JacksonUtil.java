package org.projectodd.jrapidoc.model.type;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

/**
 * Created by Tomas "sarzwest" Jiricek on 13.1.15.
 */
public class JacksonUtil {

    private static ObjectMapper objectMapper;

    public static synchronized ObjectMapper objectMapperInstance(){
        if(objectMapper == null){
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }

    public static ObjectMapper setJaxBIntrospector(){
        ObjectMapper objMap = objectMapperInstance();
        objMap.setAnnotationIntrospector(new JaxbAnnotationIntrospector(objMap.getTypeFactory()));
        return objMap;
    }
}
