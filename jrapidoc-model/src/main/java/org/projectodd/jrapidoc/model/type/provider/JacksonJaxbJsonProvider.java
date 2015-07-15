package org.projectodd.jrapidoc.model.type.provider;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import org.projectodd.jrapidoc.model.object.type.Type;

public class JacksonJaxbJsonProvider extends JacksonJsonProvider {

    public JacksonJaxbJsonProvider() {
        super();
        objectMapper.setAnnotationIntrospector(AnnotationIntrospector.pair(new JaxbAnnotationIntrospector(objectMapper.getTypeFactory()),
                new JacksonAnnotationIntrospector()));
    }

    @Override
    public Type createType(java.lang.reflect.Type genericType) {
        return super.createType(genericType);
    }
}
