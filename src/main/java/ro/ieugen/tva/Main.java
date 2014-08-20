package ro.ieugen.tva;

import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;

import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws Exception {
        final CamelContext context = new DefaultCamelContext();

        context.addRoutes(new VatCodeListResolver());
        context.start();
        TimeUnit.SECONDS.sleep(30);
        context.stop();
    }
}
