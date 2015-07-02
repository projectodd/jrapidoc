package org.projectodd.jrapidoc.model.object;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Created by Tomas "sarzwest" Jiricek on 13.1.15.
 */
@JsonPropertyOrder({"attributeName", "typeRef", "attributeDescription", "required"})
public class BeanProperty {

    public static String ROOT_VARIABLE = "root";
    @JsonProperty("attributeName")
    String name;
    @JsonIgnore
    Class<?> type;
    String typeRef;
    @JsonProperty("attributeDescription")
    String description;
    @JsonProperty("required")
    boolean isRequired;

    public BeanProperty(String name, String typeRef, Class<?> type, String description, boolean isRequired) {
        this.name = name;
        this.type = type;
        this.typeRef = typeRef;
        this.description = description;
        this.isRequired = isRequired;
    }

    public void addType(Class<?> type){
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public Class<?> getType() {
        return type;
    }

    public String getTypeRef() {
        return typeRef;
    }

    public String getDescription() {
        return description;
    }

    public boolean isRequired() {
        return isRequired;
    }

    @Override
    public String toString() {
        return "BeanProperty{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", typeRef='" + typeRef + '\'' +
                ", description='" + description + '\'' +
                ", isRequired=" + isRequired +
                '}';
    }
}
