package org.projectodd.jrapidoc.model.param;

/**
 * Created by Tomas "sarzwest" Jiricek on 23.12.14.
 */
public class FormParam extends Param {
    public FormParam(String name, Boolean isRequired, String typeref, Type type, String description) {
        super(name, isRequired, typeref, type, description);
    }

    public static class FormParamBuilder extends ParamBuilder{

        @Override
        public FormParamBuilder setName(String name) {
            return (FormParamBuilder)super.setName(name);
        }

        @Override
        public FormParamBuilder setRequired(Boolean isRequired) {
            return (FormParamBuilder)super.setRequired(isRequired);
        }

        @Override
        public FormParamBuilder setDescription(String description) {
            return(FormParamBuilder) super.setDescription(description);
        }

        @Override
        public FormParamBuilder setTypeRef(String typeref) {
            return(FormParamBuilder) super.setTypeRef(typeref);
        }

        @Override
        public FormParam build() {
            return new FormParam(name, isRequired, typeRef, Type.FORM_PARAM, description);
        }
    }
}
