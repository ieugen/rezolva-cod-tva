package ro.ieugen.tva;

import org.apache.camel.EndpointInject;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.hamcrest.core.StringContains;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.ws.Holder;
import java.util.List;

public class SoapRequestBuilderLiveTest extends CamelTestSupport {

    @EndpointInject(uri = "direct:start")
    private ProducerTemplate start;

    @EndpointInject(uri = "mock:end")
    private MockEndpoint end;

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                from("direct:start")
                        .routeId("Perform live service call")
                        .tracing()
                        .process(new VatCodeResolver.SoapRequestBuilder())
                        .to("cxf://http://ec.europa.eu/taxation_customs/vies/services/checkVatService?" +
                                "serviceClass=eu.europa.ec.taxud.vies.services.checkvat.CheckVatPortType")
                        .to("mock:end");
            }
        };
    }

    @Test
    public void vatCheckReturnsExpectedResult() throws Exception {
        Exchange test = createExchangeWithBody("ignored");
        test.getIn().setHeader(Headers.VAT_CODE, "14080565");

        end.expectedMessageCount(1);
        start.send(test);

        end.assertIsSatisfied();

        Exchange result = end.getExchanges().get(0);
        List<Object> params = result.getIn().getBody(List.class);

        Holder<Boolean> valid = (Holder<Boolean>) params.get(4);
        Assert.assertTrue(valid.value);

        Holder<String> name = (Holder<String>) params.get(5);
        assertThat(name.value, StringContains.containsString("BRD"));

        System.out.println(params);

    }
}