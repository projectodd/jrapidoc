package org.projectodd.jrapidoc.model.type.provider;

import org.projectodd.jrapidoc.model.object.type.Type;

import java.util.Map;

/**
 * Created by Tomas "sarzwest" Jiricek on 25.3.15.
 */
public abstract class TypeProvider {

    public abstract Type createType(java.lang.reflect.Type genericType);

    public abstract Map<String, Type> getUsedTypes();
}
