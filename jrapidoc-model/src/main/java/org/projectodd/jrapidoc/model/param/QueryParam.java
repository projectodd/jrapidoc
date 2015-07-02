package org.projectodd.jrapidoc.model.param;

/**
 * Created by Tomas "sarzwest" Jiricek on 23.12.14.
 */
public class QueryParam extends Param  {
    public QueryParam(String name, Boolean isRequired, String typeref, Type type, String description) {
        super(name, isRequired, typeref, type, description);
    }

    public static class QueryParamBuilder extends ParamBuilder{
        @Override
        public QueryParamBuilder setName(String name) {
            return (QueryParamBuilder)super.setName(name);
        }

        @Override
        public QueryParamBuilder setRequired(Boolean isRequired) {
            return (QueryParamBuilder)super.setRequired(isRequired);
        }

        @Override
        public QueryParamBuilder setDescription(String description) {
            return(QueryParamBuilder) super.setDescription(description);
        }

        @Override
        public QueryParamBuilder setTypeRef(String typeref) {
            return(QueryParamBuilder) super.setTypeRef(typeref);
        }

        @Override
        public Param build() {
            return new QueryParam(name, isRequired, typeRef, Type.QUERY_PARAM, description);
        }
    }
}
