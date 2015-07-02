package org.projectodd.jrapidoc.introspector;

import airservice.services.TestService;
import org.projectodd.jrapidoc.exception.JrapidocExecutionException;
import org.projectodd.jrapidoc.exception.JrapidocFailureException;
import org.projectodd.jrapidoc.model.*;
import org.projectodd.jrapidoc.plugin.ConfigGroup;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.reflections.Reflections;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by papa on 1.5.15.
 */
public class TestSoapIntrospector {

    public static final String DEFAULT_GROUP = "http://localhost:9080/airservice/soap";
    public static final String DEFAULT_PACKAGE = "airservice.services";

    static SoapIntrospector soapIntrospector;
    static ConfigGroup configGroup;
    static List<ConfigGroup> configGroups;
    static APIModel apiModel;

    @BeforeClass
    public static void init() throws JrapidocFailureException, JrapidocExecutionException {
        soapIntrospector = new SoapIntrospector();
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

    static void createApiModel() throws JrapidocFailureException, JrapidocExecutionException {
        apiModel = soapIntrospector.createModel(null, configGroups, Thread.currentThread().getContextClassLoader(), null);
    }

    ServiceGroup getDefaultServiceGroup() {
        return apiModel.getServiceGroups().get(DEFAULT_GROUP);
    }

    @Test
    public void testResourceInclude() {
        Reflections reflections = soapIntrospector.getUnionOfIncludedPaths(new ArrayList<String>(Arrays.asList(new String[]{DEFAULT_PACKAGE})), Thread.currentThread().getContextClassLoader());
        Set<Class<?>> resourceClasses = reflections.getTypesAnnotatedWith(WebService.class);

        Assert.assertEquals(true, resourceClasses.contains(TestService.class));
    }

    @Test
    public void testResourceExclude() {
        Reflections reflections = soapIntrospector.getUnionOfIncludedPaths(new ArrayList<String>(Arrays.asList(new String[]{DEFAULT_PACKAGE})), Thread.currentThread().getContextClassLoader());
        Set<Class<?>> resourceClasses = reflections.getTypesAnnotatedWith(WebService.class);
        resourceClasses = soapIntrospector.removeExcludedResourceClasses(new ArrayList<String>(Arrays.asList(new String[]{"airservice.services.TestService"})), resourceClasses);

        Assert.assertEquals(false, resourceClasses.contains(TestService.class));
    }

    @Test
    public void testServiceNotNull() {
        Service service = getDefaultServiceGroup().getServices().get("TestServiceService");

        Assert.assertNotEquals(null, service);
    }

    @Test
    public void testMethodImplicitName() {
        Service service = getDefaultServiceGroup().getServices().get("TestServiceService");
        Method method = service.getMethods().get("test");

        Assert.assertEquals("test", method.getName());
    }

    @Test
    public void testMethodExplicitName() {
        Service service = getDefaultServiceGroup().getServices().get("TestServiceService");
        Method method = service.getMethods().get("explicitName");

        Assert.assertEquals("explicitName", method.getName());
    }

    @Test
    public void testImplicitSoapBinding() {
        Service service = getDefaultServiceGroup().getServices().get("TestServiceService");
        Method method = service.getMethods().get("test");
        SoapBinding soapBinding = method.getSoapBinding();

        Assert.assertEquals("WRAPPED", soapBinding.getParameterStyle());
        Assert.assertEquals("DOCUMENT", soapBinding.getStyle());
        Assert.assertEquals("LITERAL", soapBinding.getUse());
    }

    @Test
    public void testExplicitSoapBinding() {
        Service service = getDefaultServiceGroup().getServices().get("MessageRPCStyleService");
        Method method = service.getMethods().get("rpc");
        SoapBinding soapBinding = method.getSoapBinding();

        Assert.assertEquals("WRAPPED", soapBinding.getParameterStyle());
        Assert.assertEquals("RPC", soapBinding.getStyle());
        Assert.assertEquals("LITERAL", soapBinding.getUse());
    }

    @Test
    public void testPrecedenceSoapBinding() {
        Service service = getDefaultServiceGroup().getServices().get("serviceNameNAME");
        Method method = service.getMethods().get("test");
        SoapBinding soapBinding = method.getSoapBinding();

        Assert.assertEquals("WRAPPED", soapBinding.getParameterStyle());
        Assert.assertEquals("DOCUMENT", soapBinding.getStyle());
        Assert.assertEquals("LITERAL", soapBinding.getUse());
    }

    @Test
    public void testInheritanceSoapBinding() {
        Service service = getDefaultServiceGroup().getServices().get("serviceNameNAME");
        Method method = service.getMethods().get("longTimeProcess");
        SoapBinding soapBinding = method.getSoapBinding();

        Assert.assertEquals("BARE", soapBinding.getParameterStyle());
        Assert.assertEquals("RPC", soapBinding.getStyle());
        Assert.assertEquals("ENCODED", soapBinding.getUse());
    }

    @Test
    public void testOneWay() {
        Service service = getDefaultServiceGroup().getServices().get("JRAPIDocServiceService");
        Method method = service.getMethods().get("foo6");

        Assert.assertEquals(new ArrayList<Return>(), method.getReturnOptions());
    }

    @Test
    public void testReturnOption() {
        Service service = getDefaultServiceGroup().getServices().get("JRAPIDocServiceService");
        Method method = service.getMethods().get("foo4");
        Return returnOption = method.getReturnOptions().get(0);

        Assert.assertEquals("This is return option description", returnOption.getDescription());
        Assert.assertEquals("Description for return type", returnOption.getSoapOutputHeaders().get(0).getDescription());
        Assert.assertEquals("airservice.entity.destination.Destination", returnOption.getSoapOutputHeaders().get(0).getType().getTypeRef());
    }

    @Test
    public void testReturnOptions() {
        Service service = getDefaultServiceGroup().getServices().get("JRAPIDocServiceService");
        Method method = service.getMethods().get("foo4b");
        TransportType inSoapHeader = method.getSoapInputHeaders().get(0);
        Return returnOption = method.getReturnOptions().get(0);
        TransportType outSoapHeader = returnOption.getSoapOutputHeaders().get(0);

        Assert.assertEquals("Description of input output object", inSoapHeader.getDescription());
        Assert.assertEquals("Description of input output object", outSoapHeader.getDescription());
        Assert.assertEquals(true, inSoapHeader.getIsRequired());
        Assert.assertEquals(null, outSoapHeader.getIsRequired());
    }
}
