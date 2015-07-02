package org.projectodd.jrapidoc.model.type.provider.converter;

import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.ser.BeanPropertyWriter;
import com.fasterxml.jackson.databind.ser.std.*;
import com.fasterxml.jackson.databind.type.*;
import org.projectodd.jrapidoc.model.object.BeanProperty;
import org.projectodd.jrapidoc.model.object.type.CollectionTypeJrapidoc;
import org.projectodd.jrapidoc.model.object.type.CustomType;
import org.projectodd.jrapidoc.model.object.type.MapTypeJrapidoc;
import org.projectodd.jrapidoc.model.object.type.Type;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Tomas "sarzwest" Jiricek on 11.1.15.
 */
public class JacksonToJrapidocProcessor {

    ObjectMapper objectMapper;
    public static Map<String, Type> cache = new LinkedHashMap<String, Type>();

    public JacksonToJrapidocProcessor(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * From Jackson type creates a JRAPIDoc type, type is stored in cache
     *
     * @param jacksonType
     * @return
     */
    public Type getType(SimpleType jacksonType) {
        try {
            String signature = JacksonSignature.createSignature(jacksonType);
            CustomType type = new CustomType(jacksonType.getRawClass().getName(), signature, jacksonType.getRawClass());
            if (cache.containsKey(signature)) {
                return cache.get(signature);
            }
            cache.put(signature, type);
            ObjectWriter objectWriter = objectMapper.writerFor(jacksonType);
            Field prefetchField = objectWriter.getClass().getDeclaredField("_prefetch");
            prefetchField.setAccessible(true);
            ObjectWriter.Prefetch prefetch = (ObjectWriter.Prefetch) prefetchField.get(objectWriter);
            doIntrospection(prefetch.valueSerializer, type);
            return type;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Same as {@link #getType(com.fasterxml.jackson.databind.type.SimpleType)}
     *
     * @param jacksonType
     * @return
     */
    public Type getType(CollectionLikeType jacksonType) {
//        try {
        String signature = JacksonSignature.createSignature(jacksonType);
        JavaType contentType = jacksonType.getContentType();
        String contentSignature = JacksonSignature.createSignature(contentType);
        Class<?> containerClass = jacksonType.getRawClass();
        CollectionTypeJrapidoc type = new CollectionTypeJrapidoc(containerClass.getName(), signature, contentType.getRawClass().getName(), contentSignature);
        if (cache.containsKey(signature)) {
            return cache.get(signature);
        }
        cache.put(signature, type);
        getType(jacksonType.getContentType());
        return type;
    }

    /**
     * Same as {@link #getType(com.fasterxml.jackson.databind.type.SimpleType)}
     *
     * @param jacksonType
     * @return
     */
    public Type getType(ArrayType jacksonType) {
        String signature = JacksonSignature.createSignature(jacksonType);
        String contentSignature = JacksonSignature.createSignature(jacksonType.getContentType());
        Class<?> contentType = jacksonType.getContentType().getRawClass();
        Class<?> containerClass = jacksonType.getRawClass();
        CollectionTypeJrapidoc type = new CollectionTypeJrapidoc(containerClass.getName(), signature, contentType.getName(), contentSignature);
        if (cache.containsKey(signature)) {
            return cache.get(signature);
        }
        cache.put(signature, type);
        getType(jacksonType.getContentType());
        return type;
    }

    /**
     * Same as {@link #getType(com.fasterxml.jackson.databind.type.SimpleType)}
     *
     * @param jacksonType
     * @return
     */
    public Type getType(MapLikeType jacksonType) {
        String signature = JacksonSignature.createSignature(jacksonType);
        JavaType keyType = jacksonType.getKeyType();
        JavaType valueType = jacksonType.getContentType();
        Class<?> containerClass = jacksonType.getRawClass();
        String keySignature = JacksonSignature.createSignature(keyType);
        String valSignature = JacksonSignature.createSignature(valueType);
        MapTypeJrapidoc type = new MapTypeJrapidoc(containerClass.getName(), signature, keyType.getRawClass().getName(), keySignature, valueType.getRawClass().getName(), valSignature);
        if (cache.containsKey(signature)) {
            return cache.get(signature);
        }
        cache.put(signature, type);
        getType(keyType);
        getType(valueType);
        return type;
    }

    /**
     * Do redirection from general Jackson type to the concrete one
     *
     * @param type jackson type
     * @return JRAPIDoc type
     */
    public Type getType(JavaType type) {
        if (type instanceof SimpleType) {
            return getType((SimpleType) type);
        } else if (type instanceof CollectionType) {
            return getType((CollectionLikeType) type);
        } else if (type instanceof ArrayType) {
            return getType((ArrayType) type);
        } else if (type instanceof MapLikeType) {
            return getType((MapLikeType) type);
        }
        throw new RuntimeException("Unimplemented Jackson type: " + type);
    }

    /**
     * Do redirection from general Jackson serializer to the concrete one
     *
     * @param serializer
     * @param type
     */
    private void doIntrospection(JsonSerializer serializer, Type type) {
        if (serializer == null) {
            return;
        }
        if (EnumSerializer.class.isAssignableFrom(serializer.getClass())) {
            introspectSerializer((EnumSerializer) serializer, (CustomType) type);
        } else if (BeanSerializerBase.class.isAssignableFrom(serializer.getClass())) {
            introspectSerializer((BeanSerializerBase) serializer, (CustomType) type);
        } else if (StdScalarSerializer.class.isAssignableFrom(serializer.getClass())) {
            introspectSerializer((StdScalarSerializer) serializer, (CustomType) type);
        } else if (AsArraySerializerBase.class.isAssignableFrom(serializer.getClass())) {
            introspectSerializer((AsArraySerializerBase) serializer, (CollectionTypeJrapidoc) type);
        } else if (MapSerializer.class.isAssignableFrom(serializer.getClass())) {
            introspectSerializer((MapSerializer) serializer, (MapTypeJrapidoc) type);
        }
    }

    /**
     * Introspect serializer for collections
     *
     * @param collectionSerializer
     * @param type
     */
    private void introspectSerializer(AsArraySerializerBase collectionSerializer, CollectionTypeJrapidoc type) {
        getType(collectionSerializer.getContentType());
    }

    /**
     * Introspect serializer for java beans
     *
     * @param beanSerializer
     * @param type
     */
    private void introspectSerializer(BeanSerializerBase beanSerializer, CustomType type) {
        try {
            Field propsField = beanSerializer.getClass().getSuperclass().getDeclaredField("_props");
            propsField.setAccessible(true);
            BeanPropertyWriter[] props = (BeanPropertyWriter[]) propsField.get(beanSerializer);
            for (BeanPropertyWriter prop : props) {
                JavaType propType = prop.getType();
                getType(propType);
                String signature = JacksonSignature.createSignature(propType);
                type.addBeanProperty(new BeanProperty(prop.getName(), signature, prop.getPropertyType(), prop.getMetadata().getDescription(), prop.getMetadata().isRequired()));
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Introspect serializer for  enumerations
     *
     * @param enumSerializer
     * @param type
     */
    private void introspectSerializer(EnumSerializer enumSerializer, CustomType type) {
        for (SerializableString value : enumSerializer.getEnumValues().values()) {
            type.addEnumeration(value.getValue());
        }
    }

    /**
     * Introspect serializer for map
     *
     * @param mapSerializer
     * @param type
     */
    private void introspectSerializer(MapSerializer mapSerializer, MapTypeJrapidoc type) {
        try {
            Field keyTypeField = mapSerializer.getClass().getDeclaredField("_keyType");
            keyTypeField.setAccessible(true);
            JavaType keyType = (JavaType) keyTypeField.get(mapSerializer);
            JavaType valueType = mapSerializer.getContentType();
            getType(keyType);
            getType(valueType);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * Do nothing for this serializer
     *
     * @param stdScalarSerializer
     * @param type
     */
    private void introspectSerializer(StdScalarSerializer stdScalarSerializer, CustomType type) {
    }

    public Type loadType(JavaType javaType) {
        Type type = getType(javaType);
        return type;
    }
}
