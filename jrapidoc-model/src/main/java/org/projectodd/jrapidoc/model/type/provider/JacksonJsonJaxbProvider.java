package org.projectodd.jrapidoc.model.type.provider;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import org.projectodd.jrapidoc.model.object.type.Type;

/**
 * Created by Tomas "sarzwest" Jiricek on 11.4.15.
 */
public class JacksonJsonJaxbProvider extends JacksonJsonProvider{

    public JacksonJsonJaxbProvider() {
        super();
        objectMapper.setAnnotationIntrospector(AnnotationIntrospector.pair(new JacksonAnnotationIntrospector(), new JaxbAnnotationIntrospector(objectMapper.getTypeFactory())));
    }

    @Override
    public Type createType(java.lang.reflect.Type genericType) {
        return super.createType(genericType);
    }
}
