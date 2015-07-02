package org.projectodd.jrapidoc.introspector;

import airservice.resources.TestResource;
import org.projectodd.jrapidoc.RestUtil;
import org.projectodd.jrapidoc.exception.JrapidocFailureException;
import org.projectodd.jrapidoc.model.*;
import org.projectodd.jrapidoc.model.param.*;
import org.projectodd.jrapidoc.plugin.ConfigGroup;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.reflections.Reflections;

import javax.ws.rs.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by Tomas "sarzwest" Jiricek on 23.3.15.
 */
public class TestRestIntrospector {

    public static final String DEFAULT_GROUP = "http://localhost:9080/airservice/rest";
    public static final String DEFAULT_GROUP_DEFAULT_SERVICE = "test/{pathparam}";
    public static final String DEFAULT_GROUP_DEFAULT_SERVICE_DEFAULT_METHOD = "test/{pathparam}/pathExample/[a-z]{3} - GET";
    public static final String DEFAULT_PACKAGE = "airservice.resources";
    static RestIntrospector restIntrospector;
    static ConfigGroup configGroup;
    static List<ConfigGroup> configGroups;
    static APIModel apiModel;


    @BeforeClass
    public static void init() throws JrapidocFailureException {
        restIntrospector = new RestIntrospector();
        createConfigGroup();
        createConfigGroups();
        createApiModel();
    }

    static void createConfigGroup() {
        configGroup = new ConfigGroup();
        configGroup.setBaseUrl(DEFAULT_GROUP);
        configGroup.setIncludes(new ArrayList<String>(Arrays.asList(new String[]{DEFAULT_PACKAGE})));

    }

    static void createConfigGroups() {
        configGroups = new ArrayList<ConfigGroup>();
        configGroups.add(configGroup);
    }

    static void createApiModel() throws JrapidocFailureException {
        apiModel = restIntrospector.createModel(null, configGroups, Thread.currentThread().getContextClassLoader(), null);
    }

    ServiceGroup getDefaultServiceGroup() {
        return apiModel.getServiceGroups().get(DEFAULT_GROUP);
    }

    Service getDefaultService() {
        ServiceGroup serviceGroup = getDefaultServiceGroup();
        return serviceGroup.getServices().get(DEFAULT_GROUP_DEFAULT_SERVICE);
    }

    Method getDefaultMethod() {
        Service service = getDefaultService();
        return service.getMethods().get(DEFAULT_GROUP_DEFAULT_SERVICE_DEFAULT_METHOD);
    }

    @Test
    public void testResourceInclude(){
        Reflections reflections = restIntrospector.getUnionOfIncludedPaths(new ArrayList<String>(Arrays.asList(new String[]{DEFAULT_PACKAGE})), Thread.currentThread().getContextClassLoader());
        Set<Class<?>> resourceClasses = reflections.getTypesAnnotatedWith(Path.class);

        Assert.assertEquals(true, resourceClasses.contains(TestResource.class));
    }

    @Test
    public void testResourceExclude(){
        Reflections reflections = restIntrospector.getUnionOfIncludedPaths(new ArrayList<String>(Arrays.asList(new String[]{DEFAULT_PACKAGE})), Thread.currentThread().getContextClassLoader());
        Set<Class<?>> resourceClasses = reflections.getTypesAnnotatedWith(Path.class);
        resourceClasses = restIntrospector.removeExcludedResourceClasses(new ArrayList<String>(Arrays.asList(new String[]{"airservice.resources.TestResource"})), resourceClasses);

        Assert.assertEquals(false, resourceClasses.contains(TestResource.class));
    }

    @Test
    public void testPathExampleOnResourceClass() throws JrapidocFailureException {
        ServiceGroup serviceGroup = apiModel.getServiceGroups().get(DEFAULT_GROUP);
        Service service = serviceGroup.getServices().get("test/{pathparam}");

        Assert.assertEquals("test/5", service.getPathExample());
    }

    @Test
    public void testPathExampleOnResourceClass1() throws JrapidocFailureException {
        ServiceGroup serviceGroup = apiModel.getServiceGroups().get(DEFAULT_GROUP);
        Service service = serviceGroup.getServices().get("type");

        Assert.assertEquals(null, service.getPathExample());
    }

    @Test
    public void testPathExampleOnResourceMethod() throws JrapidocFailureException {
        ServiceGroup serviceGroup = apiModel.getServiceGroups().get(DEFAULT_GROUP);
        Service service = serviceGroup.getServices().get("test/{pathparam}");
        Method method = service.getMethods().get(RestUtil.trimSlash("test/{pathparam}/pathExample/[a-z]{3}") + " - GET");

        Assert.assertEquals("test/5/pathExample/aaa", method.getPathExample());
    }

    @Test
    public void testFieldMatrixAll() throws JrapidocFailureException {
        Method method = getDefaultMethod();
        MatrixParam matrixparam = method.getMatrixParams().get("matrixparam");

        Assert.assertEquals("matrixparam", matrixparam.getName());
        Assert.assertEquals("qwerty", matrixparam.getDescription());
        Assert.assertEquals(true, matrixparam.isRequired());
        Assert.assertEquals("string", matrixparam.getTypeRef());
    }

    @Test
    public void testFieldQueryAll() throws JrapidocFailureException {
        Method method = getDefaultMethod();
        QueryParam queryparam = method.getQueryParams().get("queryparam");

        Assert.assertEquals("queryparam", queryparam.getName());
        Assert.assertEquals("qqq", queryparam.getDescription());
        Assert.assertEquals(false, queryparam.isRequired());
        Assert.assertEquals("number", queryparam.getTypeRef());
    }

    @Test
    public void testFieldPathAll() throws JrapidocFailureException {
        Method method = getDefaultMethod();
        PathParam pathparam = method.getPathParams().get("pathparam");

        Assert.assertEquals("pathparam", pathparam.getName());
        Assert.assertEquals("www", pathparam.getDescription());
        Assert.assertEquals(true, pathparam.isRequired());
        Assert.assertEquals("number", pathparam.getTypeRef());
    }

    @Test
    public void testFieldCookieAll() throws JrapidocFailureException {
        Method method = getDefaultMethod();
        CookieParam cookieparam = method.getCookieParams().get("cookieparam");

        Assert.assertEquals("cookieparam", cookieparam.getName());
        Assert.assertEquals("eee", cookieparam.getDescription());
        Assert.assertEquals(true, cookieparam.isRequired());
        Assert.assertEquals("string", cookieparam.getTypeRef());
    }

    @Test
    public void testFieldHeaderAll() throws JrapidocFailureException {
        Method method = getDefaultMethod();
        HeaderParam headerparam = method.getHeaderParams().get("headerparam");

        Assert.assertEquals("headerparam", headerparam.getName());
        Assert.assertEquals("rrr", headerparam.getDescription());
        Assert.assertEquals(true, headerparam.isRequired());
        Assert.assertEquals("string", headerparam.getTypeRef());
    }

    @Test
    public void testFieldFormAll() throws JrapidocFailureException {
        Method method = getDefaultMethod();
        FormParam formparam = method.getFormParams().get("formparam");

        Assert.assertEquals("formparam", formparam.getName());
        Assert.assertEquals("ttt", formparam.getDescription());
        Assert.assertEquals(null, formparam.isRequired());
        Assert.assertEquals("string", formparam.getTypeRef());
    }

    @Test
    public void testFieldBeanPropertyList() throws JrapidocFailureException {
        Method method = getDefaultMethod();
        QueryParam listbean = method.getQueryParams().get("listbean");

        Assert.assertEquals("listbean", listbean.getName());
        Assert.assertEquals("asd", listbean.getDescription());
        Assert.assertEquals(null, listbean.isRequired());
        Assert.assertEquals("string", listbean.getTypeRef());
    }

    @Test
    public void testConstructorQueryAll() throws JrapidocFailureException {
        Method method = getDefaultMethod();
        QueryParam queryparam = method.getQueryParams().get("queryconst");

        Assert.assertEquals("queryconst", queryparam.getName());
        Assert.assertEquals("aaa", queryparam.getDescription());
        Assert.assertEquals(true, queryparam.isRequired());
        Assert.assertEquals("string", queryparam.getTypeRef());
    }

    @Test
    public void testSetterHeaderAll() throws JrapidocFailureException {
        Method method = getDefaultMethod();
        HeaderParam headerparam = method.getHeaderParams().get("headerFromSetter");

        Assert.assertEquals("headerFromSetter", headerparam.getName());
        Assert.assertEquals("hdsa", headerparam.getDescription());
        Assert.assertEquals(null, headerparam.isRequired());
        Assert.assertEquals("number", headerparam.getTypeRef());
    }

    @Test
    public void testMultipleHttpDesignatorsOnMethod(){
        Service service = getDefaultService();

        Assert.assertEquals(true, service.getMethods().containsKey("test/{pathparam}/encoded - GET"));
        Assert.assertEquals(true, service.getMethods().containsKey("test/{pathparam}/encoded - POST"));
    }

    @Test
    public void testConsumesProduces(){
        Service service = getDefaultService();
        Method method = service.getMethods().get("test/{pathparam}/encoded - GET");

        Assert.assertEquals("application/json", method.getHeaderParams().get("Accept").getOptions()[0]);
        Assert.assertEquals("application/xml", method.getHeaderParams().get("Accept").getOptions()[1]);
        Assert.assertEquals("application/json", method.getHeaderParams().get("Content-Type").getOptions()[0]);
    }

    @Test
    public void testTypeListString(){
        ServiceGroup serviceGroup = getDefaultServiceGroup();
        Service service = serviceGroup.getServices().get("type");
        Method methodWithParam = service.getMethods().get("type/foo1 - GET");
        Method methodWithReturn = service.getMethods().get("type/foo2 - GET");

        Assert.assertEquals("array<string>", methodWithParam.getParameters().get(0).getType().getTypeRef());
        Assert.assertEquals("array<string>", methodWithReturn.getReturnOptions().get(0).getReturnTypes().get(0).getType().getTypeRef());
    }

    @Test
    public void testTypeMapStringString(){
        ServiceGroup serviceGroup = getDefaultServiceGroup();
        Service service = serviceGroup.getServices().get("type");
        Method methodWithParam = service.getMethods().get("type/foo3 - GET");
        Method methodWithReturn = service.getMethods().get("type/foo4 - GET");

        Assert.assertEquals("map<string,string>", methodWithParam.getParameters().get(0).getType().getTypeRef());
        Assert.assertEquals("map<string,string>", methodWithReturn.getReturnOptions().get(0).getReturnTypes().get(0).getType().getTypeRef());
    }

    @Test
    public void testTypeCustomParameterized(){
        ServiceGroup serviceGroup = getDefaultServiceGroup();
        Service service = serviceGroup.getServices().get("type");
        Method methodWithParam = service.getMethods().get("type/foo5 - GET");
        Method methodWithReturn = service.getMethods().get("type/foo6 - GET");

        Assert.assertEquals("airservice.entity.destination.D1<airservice.entity.destination.DestinationOutput>", methodWithParam.getParameters().get(0).getType().getTypeRef());
        Assert.assertEquals("airservice.entity.destination.D1<airservice.entity.destination.DestinationOutput>", methodWithReturn.getReturnOptions().get(0).getReturnTypes().get(0).getType().getTypeRef());
    }

    @Test
    public void testTypeTypeVariable(){
        ServiceGroup serviceGroup = getDefaultServiceGroup();
        Service service = serviceGroup.getServices().get("type");
        Method methodWithParam = service.getMethods().get("type/foo7 - GET");
        Method methodWithReturn = service.getMethods().get("type/foo8 - GET");

        Assert.assertEquals("airservice.entity.destination.DestinationOutput", methodWithParam.getParameters().get(0).getType().getTypeRef());
        Assert.assertEquals("airservice.entity.destination.Destination", methodWithReturn.getReturnOptions().get(0).getReturnTypes().get(0).getType().getTypeRef());
    }

    @Test
    public void testTypeTypeVariable1(){
        ServiceGroup serviceGroup = getDefaultServiceGroup();
        Service service = serviceGroup.getServices().get("type");
        Method methodWithParam = service.getMethods().get("type/foo9 - GET");
        Method methodWithReturn = service.getMethods().get("type/foo10 - GET");

        Assert.assertEquals("array<airservice.entity.destination.DestinationOutput>", methodWithParam.getParameters().get(0).getType().getTypeRef());
        Assert.assertEquals("array<airservice.entity.destination.DestinationOutput>", methodWithReturn.getReturnOptions().get(0).getReturnTypes().get(0).getType().getTypeRef());
    }

    @Test
    public void testTypeTypeVariable2(){
        ServiceGroup serviceGroup = getDefaultServiceGroup();
        Service service = serviceGroup.getServices().get("type");
        Method methodWithParam = service.getMethods().get("type/foo11 - GET");
        Method methodWithReturn = service.getMethods().get("type/foo12 - GET");

        Assert.assertEquals("array<airservice.entity.destination.DestinationOutput>", methodWithParam.getParameters().get(0).getType().getTypeRef());
        Assert.assertEquals("array<airservice.entity.destination.DestinationOutput>", methodWithReturn.getReturnOptions().get(0).getReturnTypes().get(0).getType().getTypeRef());
    }

    @Test
    public void testTypeTypeVariable3(){
        ServiceGroup serviceGroup = getDefaultServiceGroup();
        Service service = serviceGroup.getServices().get("type");
        Method methodWithParam = service.getMethods().get("type/foo13 - GET");
        Method methodWithReturn = service.getMethods().get("type/foo14 - GET");

        Assert.assertEquals("java.lang.Object", methodWithParam.getParameters().get(0).getType().getTypeRef());
        Assert.assertEquals("java.lang.Object", methodWithReturn.getReturnOptions().get(0).getReturnTypes().get(0).getType().getTypeRef());
    }

    @Test
    public void testDescription(){
        Method method = getDefaultService().getMethods().get(DEFAULT_GROUP_DEFAULT_SERVICE + "/decription - POST");
        Return returnOption = method.getReturnOptions().get(0);

        Assert.assertEquals("Description of return option", returnOption.getDescription());
        Assert.assertEquals("Description of DestinationExample type", returnOption.getReturnTypes().get(0).getDescription());
    }
}
