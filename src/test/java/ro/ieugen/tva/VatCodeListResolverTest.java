package ro.ieugen.tva;

import com.google.common.io.Resources;
import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import java.nio.charset.StandardCharsets;

public class VatCodeListResolverTest extends CamelTestSupport {

    @EndpointInject(uri = "{{vatcode.input}}")
    ProducerTemplate input;

    @EndpointInject(uri = "{{vatcode.output}}")
    MockEndpoint output;

    @Override
    protected CamelContext createCamelContext() throws Exception {
        CamelContext context1 = super.createCamelContext();
        PropertiesComponent props = new PropertiesComponent();
        props.setLocation("classpath:vat-code-test.properties");
        context1.addComponent("properties", props);
        return context1;
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new VatCodeListResolver();
    }

    @Test
    public void processingVatCodeListReturnsTheNumberOfCodesInTheList() throws Exception {
        String resource = Resources.toString(Resources.getResource("vat_codes.txt"), StandardCharsets.UTF_8);
        output.expectedMessageCount(3);

        input.send(createExchangeWithBody(resource));

        output.assertIsSatisfied();
    }
}