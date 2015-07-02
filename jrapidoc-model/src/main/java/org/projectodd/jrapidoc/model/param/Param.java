package org.projectodd.jrapidoc.model.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * Created by Tomas "sarzwest" Jiricek on 23.12.14.
 */
@JsonPropertyOrder({"parameterName", "parameterDescription", "required", "type", "typeRef"})
public abstract class Param {

    public static enum Type {
        FORM_PARAM,
        QUERY_PARAM,
        MATRIX_PARAM,
        HEADER_PARAM,
        PATH_PARAM,
        COOKIE_PARAM
    };

    @JsonProperty("parameterName")
    private String name;
    @JsonProperty("required")
    private Boolean isRequired;
    @JsonIgnore
    private Type type;
    @JsonProperty("parameterDescription")
    private String description;
    private String typeRef;

    protected Param(String name, Boolean isRequired, String typeRef, Type type, String description) {
        this.name = name;
        this.isRequired = isRequired;
        this.typeRef = typeRef;
        this.type = type;
        this.description = description;
    }

    public Boolean isRequired() {
        return isRequired;
    }

    public String getTypeRef() {
        return typeRef;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTypeRef(String typeRef) {
        this.typeRef = typeRef;
    }

    public static abstract class ParamBuilder{

        String name;
        Boolean isRequired;
        String description;
        String typeRef;

        public ParamBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public ParamBuilder setRequired(Boolean isRequired) {
            this.isRequired = isRequired;
            return this;
        }

        public ParamBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        public ParamBuilder setTypeRef(String typeRef) {
            this.typeRef = typeRef;
            return this;
        }

        public abstract Param build();
    }
}
