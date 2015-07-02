package org.projectodd.jrapidoc.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.projectodd.jrapidoc.logger.Logger;
import org.projectodd.jrapidoc.model.param.CookieParam;
import org.projectodd.jrapidoc.model.param.HeaderParam;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Tomas "sarzwest" Jiricek on 23.12.14.
 */
@JsonPropertyOrder({"httpStatus", "returnDescription", "headerParams", "cookieParams",
"soapOutputHeaders", "returnTypes"})
public class Return {

    private int httpStatus;
    private Map<String, HeaderParam> headerParams;
    private Map<String, CookieParam> cookieParams;
    private List<TransportType> returnTypes;
    @JsonProperty("returnDescription")
    private String description;
    private List<TransportType> soapOutputHeaders;

    private Return(int httpStatus, Map<String, HeaderParam> headerParams, Map<String, CookieParam> cookieParams, List<TransportType> returnTypes, String description, List<TransportType> soapOutputHeaders) {
        this.httpStatus = httpStatus;
        this.headerParams = headerParams;
        this.cookieParams = cookieParams;
        this.returnTypes = returnTypes;
        this.description = description;
        this.soapOutputHeaders = soapOutputHeaders;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public Map<String, HeaderParam> getHeaderParams() {
        return headerParams;
    }

    public Map<String, CookieParam> getCookieParams() {
        return cookieParams;
    }

    public List<TransportType> getReturnTypes() {
        return returnTypes;
    }

    public String getDescription() {
        return description;
    }

    public List<TransportType> getSoapOutputHeaders() {
        return soapOutputHeaders;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public void setHeaderParams(Map<String, HeaderParam> headerParams) {
        this.headerParams = headerParams;
    }

    public void setCookieParams(Map<String, CookieParam> cookieParams) {
        this.cookieParams = cookieParams;
    }

    public void setReturnTypes(List<TransportType> returnTypes) {
        this.returnTypes = returnTypes;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSoapOutputHeaders(List<TransportType> soapOutputHeaders) {
        this.soapOutputHeaders = soapOutputHeaders;
    }

    public static class ReturnBuilder {

        private int httpStatus;
        private Map<String, HeaderParam> headerParams = new LinkedHashMap<String, HeaderParam>();
        private Map<String, CookieParam> cookieParams = new LinkedHashMap<String, CookieParam>();
        private List<TransportType> returnTypes = new ArrayList<TransportType>();
        private String description;
        private List<TransportType> soapOutputHeaders = new ArrayList<TransportType>();

        public ReturnBuilder httpStatus(int httpStatus) {
            this.httpStatus = httpStatus;
            return this;
        }

        public ReturnBuilder headerParams(List<HeaderParam> headerParams) {
            for (HeaderParam param : headerParams) {
                String key = param.getName();
                if (key == null) {
                    Logger.warn("Putting null key into map!!!");
                }
                if (this.headerParams.containsKey(key)) {
                    Logger.warn("Header param identifier must be unique, but header param with identifier {0} already exists!!!", key);
                }
                this.headerParams.put(param.getName(), param);
            }
            return this;
        }

        public ReturnBuilder cookieParams(List<CookieParam> cookieParams) {
            for (CookieParam param : cookieParams) {
                String key = param.getName();
                if (key == null) {
                    Logger.warn("Putting null key into map!!!");
                }
                if (this.cookieParams.containsKey(key)) {
                    Logger.warn("Cookie param identifier must be unique, but cookie param with identifier {0} already exists!!!", key);
                }
                this.cookieParams.put(key, param);
            }
            return this;
        }

        public ReturnBuilder returnTypes(List<TransportType> returnTypes) {
            for (TransportType type : returnTypes) {
                this.returnTypes.add(type);
            }
            return this;
        }

        public ReturnBuilder description(String description) {
            this.description = description;
            return this;
        }

        public ReturnBuilder soapOutputHeader(TransportType soapHeader) {
            this.soapOutputHeaders.add(soapHeader);
            return this;
        }

        public Return build() {
            return new Return(httpStatus, headerParams, cookieParams, returnTypes, description, (soapOutputHeaders.isEmpty()) ? null : soapOutputHeaders);
        }
    }
}
