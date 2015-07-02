package org.projectodd.jrapidoc.model.param;

/**
 * Created by Tomas "sarzwest" Jiricek on 23.12.14.
 */
public class CookieParam extends Param {
    public CookieParam(String name, Boolean isRequired, String typeref, Type type, String description) {
        super(name, isRequired, typeref, type, description);
    }

    public static class CookieParamBuilder extends ParamBuilder{

        @Override
        public CookieParamBuilder setName(String name) {
            return (CookieParamBuilder)super.setName(name);
        }

        @Override
        public CookieParamBuilder setRequired(Boolean isRequired) {
            return (CookieParamBuilder)super.setRequired(isRequired);
        }

        @Override
        public CookieParamBuilder setDescription(String description) {
            return(CookieParamBuilder) super.setDescription(description);
        }

        @Override
        public CookieParamBuilder setTypeRef(String typeref) {
            return(CookieParamBuilder) super.setTypeRef(typeref);
        }

        @Override
        public CookieParam build() {
            return new CookieParam(name, isRequired, typeRef, Type.COOKIE_PARAM, description);
        }
    }
}
