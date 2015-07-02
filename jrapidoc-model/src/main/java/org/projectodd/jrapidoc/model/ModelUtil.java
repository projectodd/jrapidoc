package org.projectodd.jrapidoc.model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Tomas "sarzwest" Jiricek on 29.3.15.
 */
public class ModelUtil {

    static Set<String> numberPrimitiveTypes = new HashSet<String>(Arrays.asList(new String[]{"byte", "short",
            "int", "long", "float", "double"}));
    static Set<String> booleanPrimitiveTypes = new HashSet<String>(Arrays.asList(new String[]{"boolean"}));
    static Set<Class<?>> booleanTypes = new HashSet<Class<?>>(Arrays.asList(new Class<?>[]{Boolean.class}));
    static Set<String> stringPrimitiveTypes = new HashSet<String>(Arrays.asList(new String[]{"char"}));
    static Set<Class<?>> stringTypes = new HashSet<Class<?>>(Arrays.asList(new Class<?>[]{String.class}));


    public static String getSimpleTypeSignature(Class<?> clazz, List<Class<?>> parameterClasses) {
        if (isNumericType(clazz)) {
            return "number";
        }
        if (isBooleanType(clazz)) {
            return "boolean";
        }
        if (isStringType(clazz)) {
            return "string";
        }
        if (parameterClasses.isEmpty()) {
            return clazz.getCanonicalName();
        }
        String sign = clazz.getCanonicalName() + "<";
        for (int i = 0; i < parameterClasses.size() - 1; i++) {
            sign += parameterClasses.get(i).getCanonicalName() + ",";
        }
        sign += parameterClasses.get(parameterClasses.size() - 1).getCanonicalName() + ">";
        return sign;
    }

    public static boolean isStringType(Class<?> clazz) {
        return stringPrimitiveTypes.contains(clazz.getName()) || stringTypes.contains(clazz);
    }

    public static boolean isBooleanType(Class<?> clazz) {
        return booleanPrimitiveTypes.contains(clazz.getName()) || booleanTypes.contains(clazz);
    }

    public static boolean isNumericType(Class<?> clazz) {
        return numberPrimitiveTypes.contains(clazz.getName()) || Number.class.isAssignableFrom(clazz);
    }
}