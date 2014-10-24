package ro.ieugen.tva;

import org.apache.camel.CamelContext;
import org.apache.camel.TypeConverter;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.core.osgi.OsgiCamelContextHelper;
import org.apache.camel.core.osgi.OsgiFactoryFinderResolver;
import org.apache.camel.core.osgi.OsgiTypeConverter;
import org.apache.camel.core.osgi.utils.BundleContextUtils;
import org.apache.camel.core.osgi.utils.BundleDelegatingClassLoader;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.spi.FactoryFinder;
import org.apache.camel.spi.Registry;
import org.apache.camel.util.LoadPropertiesException;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

@Component(immediate = true, service = CamelContext.class,
        // use this value: file:${karaf.home}/etc/vat-code-prod.properties for loading from etc
        property = {"propertyComponent.locations=vat-code-prod.properties"}
)
public class ScrCamelContext extends DefaultCamelContext {

    private BundleContext bundleContext;
    private Registry registry;

    @Activate
    void activate(ComponentContext context, Map<String, Object> config) throws Exception {
        bundleContext = context.getBundleContext();
        OsgiCamelContextHelper.osgiUpdate(this, bundleContext);
        // setup the application context classloader with the bundle classloader
        setApplicationContextClassLoader(new BundleDelegatingClassLoader(bundleContext.getBundle()));

        String locations = (String) config.get("propertyComponent.locations");
        if (locations == null) {
            locations = "vat-code-prod.properties";
        }
        addComponent("properties", new PropertiesComponent(locations));

        start();
    }

    @Deactivate
    void deactivate() throws Exception {
        stop();
    }

    @Override
    public Map<String, Properties> findComponents() throws LoadPropertiesException, IOException {
        return BundleContextUtils.findComponents(bundleContext, this);
    }

    @Override
    public String getComponentDocumentation(String componentName) throws IOException {
        return BundleContextUtils.getComponentDocumentation(bundleContext, this, componentName);
    }

    @Override
    protected Registry createRegistry() {
        if (registry != null) {
            return OsgiCamelContextHelper.wrapRegistry(this, registry, bundleContext);
        } else {
            return OsgiCamelContextHelper.wrapRegistry(this, super.createRegistry(), bundleContext);
        }
    }

    @Override
    protected TypeConverter createTypeConverter() {
        // CAMEL-3614: make sure we use a bundle context which imports org.apache.camel.impl.converter package
        BundleContext ctx = BundleContextUtils.getBundleContext(getClass());
        if (ctx == null) {
            ctx = bundleContext;
        }
        FactoryFinder finder = new OsgiFactoryFinderResolver(bundleContext).resolveDefaultFactoryFinder(getClassResolver());
        return new OsgiTypeConverter(ctx, getInjector(), finder);
    }
}
