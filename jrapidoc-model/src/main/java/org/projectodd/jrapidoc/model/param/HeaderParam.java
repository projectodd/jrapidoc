package org.projectodd.jrapidoc.model.param;

/**
 * Created by Tomas "sarzwest" Jiricek on 23.12.14.
 */
public class HeaderParam extends Param  {

    String[] options;
    public static final String ACCEPT = "Accept";
    public static final String CONTENT_TYPE = "Content-Type";

    private HeaderParam(String name, Boolean isRequired, String typeref, String[] options, Type type, String description) {
        super(name, isRequired, typeref, type, description);
        this.options = options;
    }

    public String[] getOptions() {
        return options;
    }

    public void setOptions(String[] options) {
        this.options = options;
    }

    public static class HeaderParamBuilder extends ParamBuilder{

        String[] options;

        public HeaderParamBuilder setOptions(String[] options) {
            this.options = options;
            return this;
        }

        @Override
        public HeaderParamBuilder setName(String name) {
            return (HeaderParamBuilder)super.setName(name);
        }

        @Override
        public HeaderParamBuilder setRequired(Boolean isRequired) {
            return (HeaderParamBuilder)super.setRequired(isRequired);
        }

        @Override
        public HeaderParamBuilder setDescription(String description) {
            return(HeaderParamBuilder) super.setDescription(description);
        }

        @Override
        public HeaderParamBuilder setTypeRef(String typeref) {
            return(HeaderParamBuilder) super.setTypeRef(typeref);
        }

        @Override
        public HeaderParam build() {
            return new HeaderParam(name, isRequired, typeRef, options, Type.HEADER_PARAM, description);
        }
    }
}
