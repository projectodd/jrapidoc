package org.projectodd.jrapidoc.model.type.provider.converter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.type.*;
import org.projectodd.jrapidoc.model.ModelUtil;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomas "sarzwest" Jiricek on 29.3.15.
 */
public class JacksonSignature {

    public static String createSignature(JavaType jacksonType) {
        if (jacksonType instanceof SimpleType) {
            return createSignature((SimpleType) jacksonType);
        } else if (jacksonType instanceof CollectionType) {
            return createSignature((CollectionLikeType) jacksonType);
        } else if (jacksonType instanceof ArrayType) {
            return createSignature((ArrayType) jacksonType);
        } else if (jacksonType instanceof MapLikeType) {
            return createSignature((MapLikeType) jacksonType);
        }
        throw new RuntimeException("Type not implemented: " + jacksonType);
    }

    public static String createSignature(SimpleType simpleType) {
        List<Class<?>> parameterClasses = null;
        try {
            Field typeParametersField = simpleType.getClass().getDeclaredField("_typeParameters");
            typeParametersField.setAccessible(true);
            JavaType[] typeParameters = (JavaType[]) typeParametersField.get(simpleType);
            parameterClasses = new ArrayList<Class<?>>();
            if (typeParameters != null) {
                for (JavaType javaType : typeParameters) {
                    parameterClasses.add(javaType.getRawClass());
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return ModelUtil.getSimpleTypeSignature(simpleType.getRawClass(), parameterClasses);
    }

    public static String createSignature(CollectionLikeType collectionLikeType) {
        return "array<" + createSignature(collectionLikeType.getContentType()) + ">";
    }

    public static String createSignature(ArrayType arrayType) {
        return "array<" + createSignature(arrayType.getContentType()) + ">";
    }

    public static String createSignature(MapLikeType mapLikeType) {
        return "map<" + createSignature(mapLikeType.getKeyType()) + "," + createSignature(mapLikeType.getContentType()) + ">";
    }
}
