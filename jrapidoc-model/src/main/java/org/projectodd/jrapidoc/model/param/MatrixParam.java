package org.projectodd.jrapidoc.model.param;

/**
 * Created by Tomas "sarzwest" Jiricek on 23.12.14.
 */
public class MatrixParam extends Param  {

    public MatrixParam(String name, Boolean isRequired, String typeref, Type type, String description) {
        super(name, isRequired, typeref, type, description);
    }

    public static class MatrixParamBuilder extends ParamBuilder{
        @Override
        public MatrixParamBuilder setName(String name) {
            return (MatrixParamBuilder)super.setName(name);
        }

        @Override
        public MatrixParamBuilder setRequired(Boolean isRequired) {
            return (MatrixParamBuilder)super.setRequired(isRequired);
        }

        @Override
        public MatrixParamBuilder setDescription(String description) {
            return(MatrixParamBuilder) super.setDescription(description);
        }

        @Override
        public MatrixParamBuilder setTypeRef(String typeref) {
            return(MatrixParamBuilder) super.setTypeRef(typeref);
        }

        @Override
        public Param build() {
            return new MatrixParam(name, isRequired, typeRef, Type.MATRIX_PARAM, description);
        }
    }
}
