package ro.ieugen.tva;

import com.google.common.io.Resources;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;
import ro.ieugen.tva.csv.VatRecord;

import java.nio.charset.StandardCharsets;

import static org.hamcrest.CoreMatchers.is;

public class CsvParserTest extends CamelTestSupport {

    @EndpointInject(uri = "direct:start")
    ProducerTemplate start;

    @EndpointInject(uri = "mock:end")
    MockEndpoint end;

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                BindyCsvDataFormat csvDataFormat = new BindyCsvDataFormat(VatRecord.class);

                from("direct:start")
                        .routeId("Parse vat code csv file")
                        .unmarshal(csvDataFormat)
                        .split(simple("${in.body}"))
                        .log("Received record ${in.body}")
                        .to("mock:end");
            }
        };
    }

    @Test
    public void unmarshallCsvFileReturns45VatRecords() throws Exception {
        String records = Resources.toString(Resources.getResource("vat_codes.csv"), StandardCharsets.UTF_8);

        end.expectedMessageCount(45);
        start.send(createExchangeWithBody(records));

        end.assertIsSatisfied();

        VatRecord firstRecord = end.getExchanges().get(0).getIn().getBody(VatRecord.class);
        assertThat(firstRecord.getSubmittedName(), is("\"\"IORDACHE,TROFIN SI ASOCIATII\"\""));
        assertThat(firstRecord.getVatCode(), is("RO15286459"));
    }
}
