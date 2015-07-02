package org.projectodd.jrapidoc.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.projectodd.jrapidoc.logger.Logger;
import org.projectodd.jrapidoc.model.object.type.Type;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Tomas "sarzwest" Jiricek on 23.12.14.
 */
@JsonPropertyOrder({"customInfo", "serviceGroups", "types"})
public class APIModel {

    private Map<String, String> customInfo;
    private Map<String, ServiceGroup> serviceGroups;
    private Map<String, Type> types;

    private APIModel(Map<String, ServiceGroup> serviceGroups, Map<String, Type> types, Map<String, String> customInfo) {
        this.serviceGroups = serviceGroups;
        this.types = types;
        this.customInfo = customInfo;
    }

    public void setCustomInfo(Map<String, String> customInfo) {
        this.customInfo = customInfo;
    }

    public void setTypes(Map<String, Type> types) {
        this.types = types;
    }

    public void setServiceGroups(Map<String, ServiceGroup> serviceGroups) {
        this.serviceGroups = serviceGroups;
    }

    public Map<String, String> getCustomInfo() {
        return customInfo;
    }

    public Map<String, Type> getTypes() {
        return types;
    }

    public Map<String, ServiceGroup> getServiceGroups() {
        return serviceGroups;
    }

    public static class APIModelBuilder {
        private Map<String, ServiceGroup> resourceGroups = new TreeMap<String, ServiceGroup>();
        private Map<String, String> customInfo = new LinkedHashMap<String, String>();
        private Map<String, Type> types;

        public APIModelBuilder resourceGroup(ServiceGroup serviceGroup) {
            String key = serviceGroup.getBaseUrl();
            Logger.debug("Service group identifier: {0}", key);
            if(key == null){
                Logger.warn("Putting null key into map!!!");
            }
            if(resourceGroups.containsKey(key)){
                Logger.warn("Resource group identifier must be unique, but resource group with identifier {0} already exists!!!", key);
            }
            this.resourceGroups.put(serviceGroup.getBaseUrl(), serviceGroup);
            return this;
        }

        public APIModelBuilder types(Map<String, Type> types) {
            this.types = types;
            return this;
        }

        public APIModelBuilder customInfo(String key, String value) {
            this.customInfo.put(key, value);
            return this;
        }

        public APIModel build(){
            return new APIModel(resourceGroups, types, (customInfo.isEmpty())? null: customInfo);
        }
    }
}
