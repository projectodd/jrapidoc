package org.projectodd.jrapidoc.plugin;

import java.util.List;

public class ConfigGroup {

    private String baseUrl;
    private String description;
    private String target;
    private List<String> includes;
    private List<String> excludes;

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public void setIncludes(List<String> includes) {
        this.includes = includes;
    }

    public void setExcludes(List<String> excludes) {
        this.excludes = excludes;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getTarget() {
        return target;
    }

    public List<String> getIncludes() {
        return includes;
    }

    public List<String> getExcludes() {
        return excludes;
    }

    public String getDescription() {
        return description;
    }
}
