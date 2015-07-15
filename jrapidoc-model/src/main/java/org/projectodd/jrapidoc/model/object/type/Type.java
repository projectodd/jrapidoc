package org.projectodd.jrapidoc.model.object.type;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Type {

    private String typeRef;
    
    @JsonIgnore
    private String typeName;

    protected Type(String typeName, String typeRef) {
        this.typeName = typeName;
        this.typeRef = typeRef;
    }

    public String getTypeRef() {
        return typeRef;
    }

    public String getTypeName() {
        return typeName;
    }

    @Override
    public String toString() {
        return "Type{" + "typeRef='" + typeRef + '\'' + ", typeName='" + typeName + '\'' + '}';
    }
}
