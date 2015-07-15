package org.projectodd.jrapidoc.model.type.provider;

import org.projectodd.jrapidoc.model.object.type.Type;

import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;

public class JacksonJaxbProvider extends JacksonJsonProvider {

    public JacksonJaxbProvider() {
        super();
        objectMapper.setAnnotationIntrospector(new JaxbAnnotationIntrospector(objectMapper.getTypeFactory()));
    }

    @Override
    public Type createType(java.lang.reflect.Type genericType) {
        return super.createType(genericType);
    }
}
