package ro.ieugen.tva;

import org.apache.camel.CamelContext;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws Exception {
        final CamelContext context = new DefaultCamelContext();

        PropertiesComponent props = new PropertiesComponent();
        props.setLocation("classpath:vat-code-prod.properties");
        context.addComponent("properties", props);

        context.addRoutes(new VatCodeResolver());
        context.start();
        TimeUnit.SECONDS.sleep(30);
        context.stop();
    }
}
