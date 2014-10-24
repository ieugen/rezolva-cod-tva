package ro.ieugen.tva.web.services;

import lombok.extern.slf4j.Slf4j;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Map;

// enable when pax-web can change port via Config-Admin - PAXWEB-742
//@Component(immediate = true)
@Slf4j
public class HerokuHttpServicePortConfig {

    private ConfigurationAdmin cm;

    @Activate
    public void activate(Map<String, Object> config0) throws IOException {
        Configuration config = cm.getConfiguration("org.ops4j.pax.web", null);

        String port = System.getenv("PORT");
        if (port != null) {
            try {
                int result = Integer.parseInt(port);
                Dictionary props = config.getProperties();
                props.put("org.osgi.service.http.port", result);
                log.info("Configuring OSGi Http service with port", port);
                config.update();
            } catch (NumberFormatException e) {
                log.warn("Port is not a number {}", port, e);
            }
        }
    }

    @Reference(unbind = "unbind")
    void bind(ConfigurationAdmin configurationAdmin) {
        this.cm = configurationAdmin;
    }

    void unbind(ConfigurationAdmin configurationAdmin) {
        if (cm == configurationAdmin) {
            cm = null;
        }
    }
}
