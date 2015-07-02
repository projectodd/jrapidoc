package org.jboss.resteasy.spi.metadata;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class ResourceClass {
    private static final FieldParameter[] EMPTY_FIELD_PARAMS = {};
    private static final SetterParameter[] EMPTY_SETTER_PARAMETERS = {};
    private static final ResourceMethod[] EMPTY_RESOURCE_METHODS = {};
    private static final ResourceLocator[] EMPTY_RESOURCE_LOCATORS = {};

    protected Class<?> clazz;
    protected FieldParameter[] fields = EMPTY_FIELD_PARAMS;
    protected SetterParameter[] setters = EMPTY_SETTER_PARAMETERS;
    protected ResourceMethod[] resourceMethods = EMPTY_RESOURCE_METHODS;
    protected ResourceLocator[] resourceLocators = EMPTY_RESOURCE_LOCATORS;
    protected ResourceConstructor constructor; // only one allowed
    protected String path;
    protected String pathExample;
    protected String description;
    protected String[] consumes;
    protected String[] produces;

    public ResourceClass(Class<?> clazz, String path) {
        this.clazz = clazz;
        this.path = path;
    }

    public void setPathExample(String pathExample) {
        this.pathExample = pathExample;
    }

    public String[] getConsumes() {
        return consumes;
    }

    public void setConsumes(String[] consumes) {
        this.consumes = consumes;
    }

    public String[] getProduces() {
        return produces;
    }

    public void setProduces(String[] produces) {
        this.produces = produces;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setConstructor(ResourceConstructor constructor) {
        this.constructor = constructor;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPathExample() {
        return pathExample;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public ResourceConstructor getConstructor() {
        return constructor;
    }

    public FieldParameter[] getFields() {
        return fields;
    }

    public SetterParameter[] getSetters() {
        return setters;
    }

    public ResourceMethod[] getResourceMethods() {
        return resourceMethods;
    }

    public ResourceMethod getResourceMethodByPath(String path) {
        for (ResourceMethod method:resourceMethods){
            if(method.getPath().equals(path)){
                return method;
            }
        }
        return null;
    }

    public ResourceLocator[] getResourceLocators() {
        return resourceLocators;
    }

}
