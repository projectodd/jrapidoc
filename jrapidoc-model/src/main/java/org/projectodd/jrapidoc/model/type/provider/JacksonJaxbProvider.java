package org.projectodd.jrapidoc.model.type.provider;

import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import org.projectodd.jrapidoc.model.object.type.Type;

/**
 * Created by Tomas "sarzwest" Jiricek on 25.3.15.
 */
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
