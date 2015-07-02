package org.projectodd.jrapidoc.model.object.type;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Tomas "sarzwest" Jiricek on 18.1.15.
 */
public class CollectionTypeJrapidoc extends org.projectodd.jrapidoc.model.object.type.Type {

    @JsonIgnore
    private String includeType;
    private String includeTypeRef;

    public CollectionTypeJrapidoc(String typeName, String typeRef, String includeType, String includeTypeRef) {
        super(typeName, typeRef);
        this.includeType = includeType;
        this.includeTypeRef = includeTypeRef;
    }

    public String getIncludeType() {
        return includeType;
    }

    public void setIncludeTypeRef(String includeTypeRef) {
        this.includeTypeRef = includeTypeRef;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        CollectionTypeJrapidoc that = (CollectionTypeJrapidoc) o;

        if (!includeTypeRef.equals(that.includeTypeRef)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + includeTypeRef.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "CollectionTypeApition{" +
                "includeType='" + includeType + '\'' +
                ", includeTypeRef='" + includeTypeRef + '\'' +
                "} " + super.toString();
    }
}
