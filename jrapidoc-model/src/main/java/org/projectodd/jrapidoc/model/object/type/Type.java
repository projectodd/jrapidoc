package org.projectodd.jrapidoc.model.object.type;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.projectodd.jrapidoc.model.object.BeanProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tomas "sarzwest" Jiricek on 23.12.14.
 */
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
        return "Type{" +
                "typeRef='" + typeRef + '\'' +
                ", typeName='" + typeName + '\'' +
                '}';
    }
}
