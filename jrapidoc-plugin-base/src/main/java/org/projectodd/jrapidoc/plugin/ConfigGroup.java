package org.projectodd.jrapidoc.plugin;

import java.util.List;
import java.util.Map;

/**
 * Created by Tomas "sarzwest" Jiricek on 21.4.15.
 */
public class ConfigGroup {

    private String baseUrl;
    private String description;
    private List<String> includes;
    private List<String> excludes;

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setDescription(String description) {
        this.description = description;
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
