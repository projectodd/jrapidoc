# JRAPIDoc
##### Remote API Reference Generator for JEE Applications
JRAPIDoc tool focuses on analyzing and documenting your remote API, based on JAX-RS 2.0 and JAX-WS 2.0. Yours project API documentation can be generated automatically during build process and will be always up to date. No changes to your source code are required, but can be optionally added JRAPIDoc annotations into your API for more specific and better documentation.

Process of generating API documentation composes of these consecutive steps: introspection of remote API, creation of remote API model and serializing API model in JSON format to file. Custom code can be plugged-in for analysis and modification of API model before serialization to file is processed.

###Artifacts
#####Rest Plugin
Responsible for generating API documentation for RESTful web services build with JAX-RS 2.0.
#####SOAP Plugin
Responsible for generating API documentation for SOAP-based web services build with JAX-WS 2.0.
#####GUI Client
HTML, CSS, JavaScript client responsible for presenting API documentation.
#####Annotations
Library containing additional annotations for usage in APIs based on JAX-RS and JAX-WS. Helps with creation more specific and better API documentation.

###Getting Started
1. Configure plugin/s
<li>Copy GUI Client to projects `${project.basedir}/src/main/webapp/jrapidoc`</li>
<li>Add JRAPIDoc annotations dependency and annotate remote API with them - optional</li>
<li>Run project build</li>
<li>Take API model file/s from `${project.build.directory}/generated-sources/jrapidoc`</li>
<li>Put API model file/s to `jrapidoc` directory in WAR file</li>
<li>Deploy WAR to JEE Application Server</li>
<li>In web browser locate applications context root and add suffix `/jrapidoc`</li>
<li>JRAPIDoc HTML client appears</li>
<li>Enjoy the API documentation!!!</li>

###Integration to Maven build lifecycle
Tool is currently in SNAPSHOT version and was not published in Maven central repository yet. For using it, here is workaround to download and build JRAPIDoc locally on your machine. Follow these steps to get local build:
```
$ git clone git@github.com:sarzwest/jrapidoc.git
$ cd jrapidoc
$ mvn install -Prelease
```
####Add Rest Plugin or SOAP Plugin or both to your project build lifecycle
Put code below in WAR module's `pom.xml`. For SOAP Plugin replace `rest` with `soap` in `artifactId` element.
#####Plugin Configuration
```xml
<plugin>
    <groupId>org.projectodd.jrapidoc</groupId>
    <artifactId>jrapidoc-rest-plugin</artifactId>
    <version>0.0.1</version>
    <executions>
        <execution>
            <id>run</id>
            <goals>
                <goal>run</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <groups>
            <group>
                <baseUrl>http://localhost:8080/example</baseUrl>
                <description>Publicly provided REST API</description>
                <includes>
                    <include>org.example.resource</include>
                </includes>
            </group>
        </groups>
    </configuration>
</plugin>
```
#####Getting GUI Client
GUI Clients are distributed in archive files [in this resource](jrapidoc-gui/output)
#####Adding annotations
Add to `pom.xml` code below
```xml
<dependency>
    <groupId>org.projectodd.jrapidoc</groupId>
    <artifactId>jrapidoc-annotation</artifactId>
    <version>0.0.1</version>
</dependency>
```
#####Annotate your API
Examples how API can be annotated. For more examples check [RESTful web services unit tests](jrapidoc-rest-plugin/src/test/java), [SOAP-based web services unit tests](jrapidoc-soap-plugin/src/test/java) and [example project](https://github.com/sarzwest/jrapidoc-example-app).

RESTful examples
```java
@PUT
@Path("path/example/{id:.+}")
@DocPathExample("path/example/5")
@DocDescription("Description of method")
@DocReturn(http = 200,
        description = "Description of this response",
        headers = {"X-Custom", "Y-Custom"},
        cookies = {"token"},
        type = Destination.class,
        typeDescription = "Description of returned type in response"
)
public Response pathExampleAnnotation(@DocDescription("Parameter is not required")@DocIsRequired(false)@MatrixParam("matrixParam")String m) {
    return Response.status(Response.Status.OK).header("X-Custom", "example").header("Y-Custom", "example")
            .cookie(new NewCookie("token", "example")).entity(new Destination()).build();
}
```
or
```java
@PUT
@Path("multiple/responses")
@DocReturns({
        @DocReturn(http = 200,
                description = "Returns array of DestinationEntity",
                type = DestinationEntity.class,
                structure = DocReturn.Structure.ARRAY),
        @DocReturn(http = 200,
                description = "Returns map<string,Destination>",
                type = Destination.class,
                structure = DocReturn.Structure.MAP),
        @DocReturn(http = 403,
                description = "Throw exception",
                type = CustomException.class)
})
public Response multipleResponses() throws CustomException;
```
SOAP-based examples
```java
@WebResult(name = "customName")
@DocDescription("This is operation description")
@DocReturns({
        @DocReturn(description = "This is return option description", typeDescription = "Description for return type"),
        @DocReturn(http = 500, description = "This is second return option description", type = AirserviceFault.class, typeDescription = "Description of exception type")}
)
public Destination foo4a();
```
or
```java
@WebResult(name = "customName")
@DocDescription("Example operation")
@DocReturns({
        @DocReturn(description = "Expected result of operation", typeDescription = "Represents flight destination"),
        @DocReturn(http = 500, description = "When flight ID is not valid", type = AirserviceFault.class, typeDescription = "Business logic exception")}
)
public Destination foo4b(@DocIsRequired @DocDescription("ID of flight") @WebParam(mode = WebParam.Mode.INOUT, header = true) Holder<String> flightId)throws AirserviceFault;
```
###Published example
Example API documentation is presented [here](http://sarzwest.github.io/jrapidoc-example-app/).

###Advanced

####Configuration
Full configuration goes here
```xml
<plugin>
    <groupId>org.projectodd.jrapidoc</groupId>
    <artifactId>jrapidoc-soap-plugin</artifactId>
    <version>${jrapidoc.version}</version>
    <executions>
        <execution>
            <goals>
                <goal>run</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <!--optional-->
        <custom>
            <appVersion>1.0</appVersion>
            <developerName>Sarzwest</developerName>
            <hereCanBePlacedInfinityCustomProperties>Hello :-)</hereCanBePlacedInfinityCustomProperties>
        </custom>
        <groups>
            <group>
                <baseUrl>http://localhost:8080/jrapidoc-example-app-1.0/jaxws/services</baseUrl>
                <!--optional-->
                <description>Not RPC web services</description>
                <includes>
                    <include>org</include>
                </includes>
                <!--optional-->
                <excludes>
                    <exclude>org.projectodd.jrapidoc.example.service.MessageRPCStyle</exclude>
                </excludes>
            </group>
            <group>
                <baseUrl>http://localhost:8080/jrapidoc-example-app-1.0/jaxws/services/rpc</baseUrl>
                <!--optional-->
                <description>Only RPC web services</description>
                <includes>
                    <include>org</include>
                </includes>
                <!--optional-->
                <excludes>
                    <exclude>org.projectodd.jrapidoc.example.service.Simple</exclude>
                    <exclude>org.projectodd.jrapidoc.example.service.WithJRAPIDocAnnotations</exclude>
                    <exclude>also.package.name.can.be.placed.here</exclude>
                </excludes>
            </group>
        </groups>
        <!--optional-->
        <modelHandlers>
            <modelHandler>org.projectodd.jrapidoc.example.custom.AvoidSoapOneWayMethodsHandler</modelHandler>
        </modelHandlers>
        <!--optional, here can be placed one of type provider class, which is responsible for creating data types -->
        <!--for REST is default type provider org.projectodd.jrapidoc.model.type.provider.JacksonJsonProvider-->
        <!--for SOAP is default type provider org.projectodd.jrapidoc.model.type.provider.JacksonJaxbProvider-->
        <!--<typeProviderClass>org.projectodd.jrapidoc.model.type.provider.JacksonJaxbJsonProvider</typeProviderClass>-->
        <!--<typeProviderClass>org.projectodd.jrapidoc.model.type.provider.JacksonJaxbProvider</typeProviderClass>-->
        <!--<typeProviderClass>org.projectodd.jrapidoc.model.type.provider.JacksonJsonJaxbProvider</typeProviderClass>-->
        <!--<typeProviderClass>org.projectodd.jrapidoc.model.type.provider.JacksonJsonProvider</typeProviderClass>-->
        <!--You can also implement your own type provider. If needed, you have to inherit class below-->
        <!--<typeProviderClass>org.projectodd.jrapidoc.model.type.provider.TypeProvider</typeProviderClass>-->
    </configuration>
</plugin>
```
####Custom API model processing
Simple example checks if there are REST methods without set description, if yes log this to console. In model processing can HandlerException been throwed. See javadoc and [example project](https://github.com/sarzwest/jrapidoc-example-app) for more informations.
```java
public class CheckRestMethodDescriptionHandler implements ModelHandler {
    @Override
    public void handleModel(APIModel model) throws HandlerException {
        for (ServiceGroup serviceGroup : model.getServiceGroups().values()) {
            for (Service service : serviceGroup.getServices().values()) {
                for (Method method : service.getMethods().values()) {
                    if (StringUtils.isEmpty(method.getDescription())) {
                        Logger.warn("Method {0} has not set description",
                                method.getPath());
                    }
                }
            }
        }
    }
}
```
