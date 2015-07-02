package org.projectodd.jrapidoc.model.object.type;


import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Created by Tomas "sarzwest" Jiricek on 19.1.15.
 */
public class MapTypeJrapidoc extends org.projectodd.jrapidoc.model.object.type.Type {

    @JsonIgnore
    private String keyType;
    private String keyTypeRef;
    @JsonIgnore
    private String valueType;
    private String valueTypeRef;

    public MapTypeJrapidoc(String typeName, String typeRef, String keyType, String keyTypeRef, String valueType, String valueTypeRef) {
        super(typeName, typeRef);
        this.keyType = keyType;
        this.keyTypeRef = keyTypeRef;
        this.valueType = valueType;
        this.valueTypeRef = valueTypeRef;
    }
}
