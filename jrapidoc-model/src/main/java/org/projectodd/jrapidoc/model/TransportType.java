package org.projectodd.jrapidoc.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.projectodd.jrapidoc.model.object.type.Type;

/**
 * Created by Tomas "sarzwest" Jiricek on 10.4.15.
 */
@JsonPropertyOrder({"typeDescription", "required", "type"})
public class TransportType {

    Type type;
    @JsonProperty("typeDescription")
    String description;
    @JsonProperty("required")
    Boolean isRequired;

    private TransportType(Type type, String description, Boolean isRequired) {
        this.type = type;
        this.description = description;
        this.isRequired = isRequired;
    }

    public Type getType() {
        return type;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getIsRequired() {
        return isRequired;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIsRequired(Boolean isRequired) {
        this.isRequired = isRequired;
    }

    public static class TransportTypeBuilder{
        Type type;
        String description;
        Boolean isRequired;

        public TransportTypeBuilder type(Type type) {
            this.type = type;
            return this;
        }

        public TransportTypeBuilder description(String description) {
            this.description = description;
            return this;
        }

        public TransportTypeBuilder isRequired(Boolean isRequired) {
            this.isRequired = isRequired;
            return this;
        }

        public TransportType build(){
            return new TransportType(type, description, isRequired);
        }

    }
}
