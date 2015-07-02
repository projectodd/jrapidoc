package org.projectodd.jrapidoc.plugin;

import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.descriptor.PluginDescriptor;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;
import org.codehaus.plexus.component.annotations.Component;
import org.projectodd.jrapidoc.exception.JrapidocExecutionException;
import org.projectodd.jrapidoc.exception.JrapidocFailureException;
import org.projectodd.jrapidoc.logger.Logger;
import org.projectodd.jrapidoc.introspector.SoapIntrospector;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Adapter pattern between Maven and {@link org.projectodd.jrapidoc.introspector.SoapIntrospector}<br/>
 * <br/>
 * Created by Tomas "sarzwest" Jiricek on 14.3.15.<br/>
 */
@Mojo(name = "run", defaultPhase = LifecyclePhase.PROCESS_CLASSES,
        requiresDependencyResolution = ResolutionScope.RUNTIME
)
@Component(role = SoapMojoAdapter.class)
public class SoapMojoAdapter extends AbstractMojo {

    public static final String MODEL_FILE_OUTPUT_PATH = "generated-sources/jrapidoc/jrapidoc.soap.model.json";

    @Parameter(defaultValue = "${session}", readonly = true)
    MavenSession session;

    @Parameter(defaultValue = "${project}", readonly = true)
    MavenProject project;

    @Parameter(defaultValue = "${settings}", readonly = true)
    Settings settings;

    @Parameter(defaultValue = "${project.basedir}", readonly = true)
    File basedir;

    @Parameter(defaultValue = "${project.build.directory}", readonly = true)
    File target;

    /**
     * Class implementing org.projectodd.jrapidoc.model.type.provider.TypeProvider
     */
    @Parameter(alias = "typeProviderClass", name = "typeProviderClass", property = "typeProviderClass")
    String typeProviderClass;

    /**
     * List of classes implementing org.projectodd.jrapidoc.model.handler.ModelHandler.
     * Classes are used to work with API model, such operations could be validation, changing some values in model etc.
     */
    @Parameter(alias = "modelHandlers", name = "modelHandlers", property = "modelHandlers")
    List<String> modelHandlers;

    /**
     * Map including custom property names as keys and custom values as values. These key-value pairs will be added to API model.
     */
    @Parameter(alias = "custom", name = "custom", property = "custom")
    Map<String, String> custom;

    /**
     * List of groups. Every group is identified by URI prefix and contains set of services with these prefix.
     */
    @Parameter(alias = "groups", name = "groups", property = "groups", required = true)
    List<ConfigGroup> groups;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        long start = System.currentTimeMillis();
        try {
            Logger.setLogger(getLog());
            addPluginVersionToInfo();
            List<String> classpathElements = null;
            try {
                classpathElements = project.getCompileClasspathElements();
            } catch (DependencyResolutionRequiredException e) {
                throw new JrapidocExecutionException(e.getMessage(), e);
            }
            List<URL> projectClasspathList = new ArrayList<URL>();
            for (String element : classpathElements) {
                try {
                    Logger.debug("Adding project classpath element {0}", element);
                    projectClasspathList.add(new File(element).toURI().toURL());
                } catch (MalformedURLException e) {
                    Logger.error(e, e.getMessage());
                    throw new JrapidocFailureException(element + " is an invalid classpath element", e);
                }
            }
            URL[] urls = projectClasspathList.toArray(new URL[projectClasspathList.size()]);
            SoapIntrospector soapIntrospector = new SoapIntrospector();
            soapIntrospector.run(urls, groups, typeProviderClass, new File(target, MODEL_FILE_OUTPUT_PATH), modelHandlers, custom);
        } catch (JrapidocFailureException e) {
            throw new MojoFailureException(e.getMessage(), e);
        } catch (JrapidocExecutionException e) {
            throw new MojoExecutionException(e.getMessage(), e);
        } finally {
            Logger.info("Finished in " + ((System.currentTimeMillis() - start) / 1000) + " seconds");
        }
    }

    void addPluginVersionToInfo() {
        if(custom == null){
            custom = new HashMap<String, String>();
        }
        PluginDescriptor pluginDesc = ((PluginDescriptor)getPluginContext().get("pluginDescriptor"));
        custom.put(pluginDesc.getArtifactId(), pluginDesc.getVersion());
    }
}
