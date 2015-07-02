package org.projectodd.jrapidoc.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.StringUtils;
import org.projectodd.jrapidoc.logger.Logger;
import org.projectodd.jrapidoc.model.param.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Tomas "sarzwest" Jiricek on 23.12.14.
 */
@JsonPropertyOrder({"path", "serviceName", "serviceDescription", "pathExample", "methods"})
public class Service {

    private String path;
    private String pathExample;
    @JsonIgnore
    private List<HeaderParam> headerParams = new ArrayList<HeaderParam>();
    @JsonIgnore
    private List<CookieParam> cookieParams = new ArrayList<CookieParam>();
    @JsonIgnore
    private List<FormParam> formParams = new ArrayList<FormParam>();
    @JsonIgnore
    private List<MatrixParam> matrixParams = new ArrayList<MatrixParam>();
    @JsonIgnore
    private List<PathParam> pathParams = new ArrayList<PathParam>();
    @JsonIgnore
    private List<QueryParam> queryParams = new ArrayList<QueryParam>();
    private Map<String, Method> methods = new TreeMap<String, Method>();
    @JsonProperty("serviceDescription")
    private String description;
    @JsonProperty("serviceName")
    private String name;

    private Service(String path, String pathExample, List<HeaderParam> headerParams, List<CookieParam> cookieParams, List<FormParam> formParams, List<MatrixParam> matrixParams, List<PathParam> pathParams, List<QueryParam> queryParams, Map<String, Method> methods, String description, String name) {
        this.path = path;
        this.pathExample = pathExample;
        this.headerParams = headerParams;
        this.cookieParams = cookieParams;
        this.formParams = formParams;
        this.matrixParams = matrixParams;
        this.pathParams = pathParams;
        this.queryParams = queryParams;
        this.methods = methods;
        this.description = description;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public Map<String, Method> getMethods() {
        return methods;
    }

    public String getDescription() {
        return description;
    }

    public String getPathExample() {
        return pathExample;
    }

    public List<HeaderParam> getHeaderParams() {
        return headerParams;
    }

    public List<CookieParam> getCookieParams() {
        return cookieParams;
    }

    public List<FormParam> getFormParams() {
        return formParams;
    }

    public List<MatrixParam> getMatrixParams() {
        return matrixParams;
    }

    public List<PathParam> getPathParams() {
        return pathParams;
    }

    public List<QueryParam> getQueryParams() {
        return queryParams;
    }

    private <T extends Param> T getParam(List<T> params, String paramName) {
        for (T p : params) {
            if (p.getName().equals(paramName)) {
                return p;
            }
        }
        return null;
    }

    public Param getParam(String paramType, String paramName) {
        if (paramType.equals("COOKIE_PARAM")) {
            return getParam(cookieParams, paramName);
        } else if (paramType.equals("FORM_PARAM")) {
            return getParam(formParams, paramName);
        } else if (paramType.equals("HEADER_PARAM")) {
            return getParam(headerParams, paramName);
        } else if (paramType.equals("MATRIX_PARAM")) {
            return getParam(matrixParams, paramName);
        } else if (paramType.equals("PATH_PARAM")) {
            return getParam(pathParams, paramName);
        } else if (paramType.equals("QUERY_PARAM")) {
            return getParam(queryParams, paramName);
        }
        return null;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setPathExample(String pathExample) {
        this.pathExample = pathExample;
    }

    public void setMethods(Map<String, Method> methods) {
        this.methods = methods;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class ResourceBuilder {

        private String path;
        private String pathExample;
        private List<HeaderParam> headerParams = new ArrayList<HeaderParam>();
        private List<CookieParam> cookieParams = new ArrayList<CookieParam>();
        private List<FormParam> formParams = new ArrayList<FormParam>();
        private List<MatrixParam> matrixParams = new ArrayList<MatrixParam>();
        private List<PathParam> pathParams = new ArrayList<PathParam>();
        private List<QueryParam> queryParams = new ArrayList<QueryParam>();
        private Map<String, Method> methods = new TreeMap<String, Method>();
        private String description;
        private String name;

        public ResourceBuilder path(String path) {
            this.path = path;
            return this;
        }

        public ResourceBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ResourceBuilder pathExample(String pathExample) {
            this.pathExample = pathExample;
            return this;
        }

        public ResourceBuilder method(Method method) {
            String key = (StringUtils.isNotEmpty(method.getName())) ? method.getName() : method.getPath();
            if (StringUtils.isNotEmpty(method.getHttpMethodType())) {
                key += " - " + method.getHttpMethodType();
            }
            if (key == null) {
                Logger.warn("Putting null key into map!!!");
            }
            if (this.methods.containsKey(key)) {
                Logger.warn("Method identifier must be unique, but method with identifier {0} already exists!!!", key);
            }
            this.methods.put(key, method);
            return this;
        }

        public ResourceBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ResourceBuilder param(Param.Type paramType, Param param) {
            if (paramType.equals(Param.Type.COOKIE_PARAM)) {
                addCookieParam((CookieParam) param);
            } else if (paramType.equals(Param.Type.FORM_PARAM)) {
                addFormParam((FormParam) param);
            } else if (paramType.equals(Param.Type.HEADER_PARAM)) {
                addHeaderParam((HeaderParam) param);
            } else if (paramType.equals(Param.Type.MATRIX_PARAM)) {
                addMatrixParam((MatrixParam) param);
            } else if (paramType.equals(Param.Type.PATH_PARAM)) {
                addPathParam((PathParam) param);
            } else if (paramType.equals(Param.Type.QUERY_PARAM)) {
                addQueryParam((QueryParam) param);
            }
            return this;
        }

        /**
         * First try to add more specific value and then try to add less specific value.
         */
        protected void addHeaderParam(HeaderParam headerParam) {
            for (HeaderParam param : headerParams) {
                if (param.getName().equals(headerParam.getName())) {
                    return;
                }
            }
            this.headerParams.add(headerParam);
        }

        protected void addCookieParam(CookieParam cookieParam) {
            this.cookieParams.add(cookieParam);
        }

        protected void addFormParam(FormParam formParam) {
            this.formParams.add(formParam);
        }

        protected void addMatrixParam(MatrixParam matrixParam) {
            this.matrixParams.add(matrixParam);
        }

        protected void addPathParam(PathParam pathParam) {
            this.pathParams.add(pathParam);
        }

        protected void addQueryParam(QueryParam queryParam) {
            this.queryParams.add(queryParam);
        }

        public Service build() {
            return new Service(path, pathExample, headerParams, cookieParams, formParams, matrixParams, pathParams, queryParams, methods, description, name);
        }
    }
}
