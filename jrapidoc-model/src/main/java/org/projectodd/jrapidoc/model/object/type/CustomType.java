package org.projectodd.jrapidoc.model.object.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.projectodd.jrapidoc.logger.Logger;
import org.projectodd.jrapidoc.model.object.BeanProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tomas "sarzwest" Jiricek on 18.1.15.
 */
public class CustomType extends org.projectodd.jrapidoc.model.object.type.Type {

    private Map<String, BeanProperty> attributes = new HashMap<String, BeanProperty>();
    @JsonIgnore
    private Class<?> typeClass;
    private List<String> enumerations = new ArrayList<String>();

    public CustomType(String typeName, String typeRef, Map<String, BeanProperty> attributes, Class<?> typeClass) {
        super(typeName, typeRef);
        this.attributes = attributes;
        this.typeClass = typeClass;
    }

    public CustomType(String typeName, String typeRef, Class<?> typeClass){
        super(typeName, typeRef);
        this.typeClass = typeClass;
    }

    public void addBeanProperty(BeanProperty variable) {
        attributes.put(variable.getName(), variable);
    }

    public void addEnumeration(String enumeration) {
        enumerations.add(enumeration);
    }

    @Override
    public String toString() {
        return "CustomType{" +
                "attributes=" + attributes +
                ", typeClass=" + typeClass +
                ", enumerations=" + enumerations +
                "} " + super.toString();
    }
}
