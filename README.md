# JRAPIDoc
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.projectodd.jrapidoc/jrapidoc-pom/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.projectodd.jrapidoc/jrapidoc-pom)


JRAPIDoc is remote API reference generator for JavaEE applications. This tool focuses on analyzing and documenting your remote API based on JAX-RS 2.0 and JAX-WS 2.0. Yours project API documentation can be automatically generated during the Maven build process and will be always up to date. No changes to your source code are required but JRAPIDoc annotations in form of documentation can be optionally added into your remote services.

## Artifacts
* **Rest Plugin** - generates API model for RESTful web services built with JAX-RS 2.0.
* **SOAP Plugin** - generates API model for SOAP-based web services built with JAX-WS 2.0.
* **HTML GUI** - HTML web documentation template for the remote API (uses the API model).
* **Annotations** - library containing additional annotations for more specific documentation of JAX-RS and JAX-WS services.

## Getting Started

### 1. Setup Plugin(s)
It is recommended to put the plugin below into your WAR module's `pom.xml`. Similarly, you can use `jrapidoc-soap-plugin`.

```xml
<plugin>
  <groupId>org.projectodd.jrapidoc</groupId>
  <artifactId>jrapidoc-rest-plugin</artifactId>
  <version>0.5.0.Final</version>
  <executions><execution>
    <id>run</id>
    <goals><goal>run</goal></goals>
  </execution></executions>
  <configuration>
    <modelTarget>${project.artifactId}-${project.version}/resources</modelTarget> <!-- target/example-1.0/resources -->
    <groups><group>
      <baseUrl>http://localhost:8080/example</baseUrl>
      <description>Publicly provided REST API</description>
      <includes><include>org.example.resource</include></includes>
    </group></groups>
  </configuration>
</plugin>
```

### 2. Run Project Build
The API model is generated to the `modelTarget` location during the *process-classes* lifecycle phase.

Process of generating API documentation composes of these consecutive steps:
1. Introspection of remote API.
2. Creation of remote API model.
3. Serializing API model into JSON-format file.
4. Custom code can be plugged-in for analysis and modification of API model before the serialization process.

```bash
mvn clean package
```

### 3. Add JRAPIDoc documentation annotations to your remote services
Though none documentation annotations are required, it makes the output HTML documentation complete. JRAPIDoc documentation annotations are available in the following artifact:

```xml
<dependency>
  <groupId>org.projectodd.jrapidoc</groupId>
  <artifactId>jrapidoc-annotation</artifactId>
  <version>0.5.0.Final</version>
</dependency>
```

Examples of the annotations:
* [RESTful web services unit tests](jrapidoc-rest-plugin/src/test/java)
* [SOAP-based web services unit tests](jrapidoc-soap-plugin/src/test/java)
* [Example project](https://github.com/sarzwest/jrapidoc-example-app)

### 4. Create or use example HTML Documentation
HTML documentations are distributed in the [archive files](https://github.com/projectodd/jrapidoc/releases). Next, you may decided whether you need to have the HTML documentation inside your web archive (WAR) or separately on your website.

## Published example
Example API documentation is presented [here](http://sarzwest.github.io/jrapidoc-example-app/).

