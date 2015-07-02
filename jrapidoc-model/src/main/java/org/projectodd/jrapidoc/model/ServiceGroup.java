package org.projectodd.jrapidoc.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang3.StringUtils;
import org.projectodd.jrapidoc.logger.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Tomas "sarzwest" Jiricek on 21.4.15.
 */
@JsonPropertyOrder({"baseUrl", "serviceGroupDescription", "services"})
public class ServiceGroup {

    private String baseUrl;
    @JsonProperty("serviceGroupDescription")
    private String description;
    private Map<String, Service> services = new TreeMap<String, Service>();

    private ServiceGroup(String baseUrl, String description, Map<String, Service> services) {
        this.baseUrl = baseUrl;
        this.description = description;
        this.services = services;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setServices(Map<String, Service> services) {
        this.services = services;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getDescription() {
        return description;
    }

    public Map<String, Service> getServices() {
        return services;
    }

    public static class ServiceGroupBuilder {
        private String baseUrl;
        private String description;
        private Map<String, Service> services = new TreeMap<String, Service>();

        public ServiceGroupBuilder baseUrl(String baseUrl){
            this.baseUrl = baseUrl;
            return this;
        }
        public ServiceGroupBuilder description(String description){
            this.description = description;
            return this;
        }

        public ServiceGroupBuilder service(Service service) {
            String key = (StringUtils.isNotEmpty(service.getPath()))? service.getPath(): service.getName();
            if(key == null){
                Logger.warn("Putting null key into map!!!");
            }
            if(this.services.containsKey(key)){
                Logger.warn("Service identifier must be unique, but service with identifier {0} already exists!!!", key);
            }
            this.services.put(key, service);
            return this;
        }

        public ServiceGroup build(){
            return new ServiceGroup(baseUrl, description, services);
        }
    }
}
