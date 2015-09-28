package org.projectodd.jrapidoc.model.type.provider;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.projectodd.jrapidoc.model.object.type.Type;
import org.projectodd.jrapidoc.model.type.provider.converter.JacksonToJrapidocProcessor;
import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class JacksonJsonProvider extends TypeProvider {

    protected ObjectMapper objectMapper;

    protected JacksonToJrapidocProcessor processor;

    public JacksonJsonProvider() {
        objectMapper = new ObjectMapper();
        processor = new JacksonToJrapidocProcessor(objectMapper);
    }

    @Override
    public Type createType(java.lang.reflect.Type genericType) {
        JavaType javaType = createJavaType(genericType);
        Type loadType = processor.loadType(javaType);
        return loadType;
    }

    @Override
    public Map<String, Type> getUsedTypes() {
        return JacksonToJrapidocProcessor.cache;
    }

    private JavaType createJavaType(java.lang.reflect.Type genericType) {
        JavaType javaType = null;
        if (genericType instanceof Class<?>) {
            if (((Class) genericType).isArray()) {
                javaType = objectMapper.getTypeFactory().constructArrayType(((Class) genericType).getComponentType());
            } else {
                List<Class<?>> parameterClasses = new ArrayList<Class<?>>();
                for (TypeVariable typeVariable : ((Class<?>) genericType).getTypeParameters()) {
                    parameterClasses.add(resolveBound((Class<?>) genericType, typeVariable));
                }
                javaType = objectMapper.getTypeFactory().constructParametrizedType((Class) genericType, (Class) genericType, parameterClasses.toArray(new Class<?>[0]));
            }
        } else if (genericType instanceof ParameterizedType) {
            ParameterizedType paramType = (ParameterizedType) genericType;
            java.lang.reflect.Type[] genericTypes = paramType.getActualTypeArguments();
            for (int i = 0; i < genericTypes.length; i++) {
                if (genericTypes[i] instanceof TypeVariableImpl) {
                    genericTypes[i] = ((TypeVariable) genericTypes[i]).getBounds()[0];
                }
            }
            JavaType[] javaTypes = new JavaType[genericTypes.length];
            for (int i = 0; i < javaTypes.length; i++) {
                javaTypes[i] = createJavaType(genericTypes[i]);
            }
            javaType = objectMapper.getTypeFactory().constructParametrizedType((Class) paramType.getRawType(), (Class) paramType.getRawType(),
                    javaTypes);
        } else if (genericType instanceof TypeVariable) {
            TypeVariable typeVariable = ((TypeVariable) genericType);
            java.lang.reflect.Type bound = typeVariable.getBounds()[0];
            javaType = createJavaType(bound);
        }
        return javaType;
    }

    protected static Class<?> resolveBound(Class<?> type, TypeVariable typeVariable) {
        java.lang.reflect.Type bound = typeVariable.getBounds()[0];
        if (bound instanceof Class) {
            return (Class<?>) typeVariable.getBounds()[0];
        } else if (bound instanceof TypeVariable) {
            for (java.lang.reflect.Type ancestor : type.getTypeParameters()) {
                if (ancestor.equals(bound)) {
                    return resolveBound(type, (TypeVariable)bound);
                }
            }
        }
        throw new IllegalStateException("Not yet implemented");
    }
}
