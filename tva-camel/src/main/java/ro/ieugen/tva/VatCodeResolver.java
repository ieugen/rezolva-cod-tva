package ro.ieugen.tva;

import eu.europa.ec.taxud.vies.services.checkvat.CheckVatPortType;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import ro.ieugen.tva.api.Headers;
import ro.ieugen.tva.csv.InvalidVatCodeFormat;
import ro.ieugen.tva.csv.VatRecord;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.QName;
import javax.xml.ws.Holder;
import javax.xml.ws.Service;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component(immediate = true)
public class VatCodeResolver extends RouteBuilder {

    private CamelContext camelContext;

    @Reference
    void bind(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    @Activate
    void activate() throws Exception {
        camelContext.addRoutes(this);
    }

    @Deactivate
    void deactivate() throws Exception {
        for (RouteDefinition def : this.getRouteCollection().getRoutes()) {
            log.info("Removing route {}", def.getId());
            camelContext.removeRoute(def.getId());
        }
    }

    @Override
    public void configure() throws Exception {

        BindyCsvDataFormat csvDataFormat = new BindyCsvDataFormat(VatRecord.class);

        onException(InvalidVatCodeFormat.class)
                .handled(true)
                .log("Exception validating VAT CODE ${in.headers.vatCode} - Ignored");

        from("{{vatcode.input}}")
                .routeId("VatCode_processing_route")
                .unmarshal(csvDataFormat)
                .setHeader(Headers.LIST_SIZE, new CsvLineCount())
                .split(simple("${in.body}"))
                        // make requests for each vat code
                .to("direct:make-single-vat-request")
                .aggregate(constant(true), new AggregateAsList())
                .completionSize(header(Headers.LIST_SIZE))
                .marshal(csvDataFormat)
                .to("{{vatcode.output}}");

        from("direct:make-single-vat-request")
                .routeId("Soap_request_route")
                .process(new VatCodeSoapRequest())
                .log("Processing ${in.headers.vatCode}");

    }

    public static class VatCodeSoapRequest implements Processor {

        @Override
        public void process(Exchange exchange) throws Exception {
            VatRecord record = exchange.getIn().getBody(VatRecord.class);

//            URL url = new URL("http://ec.europa.eu/taxation_customs/vies/services/checkVatService");
//            QName qName = new QName("urn:ec.europa.eu:taxud:vies:services:checkVat", "checkVatService");
//
//            Service service = Service.create(url, qName);
//            CheckVatPortType servicePort = service.getPort(CheckVatPortType.class);
//
//            Holder<Boolean> valid = new Holder<>();
//            Holder<XMLGregorianCalendar> requestDate = new Holder<>();
//            Holder<String> name = new Holder<>();
//            Holder<String> address = new Holder<>();
//
//            servicePort.checkVat(new Holder<>("RO"), new Holder<>(record.getVatCode()), requestDate, valid, name, address);
//
//            record.setRequestDate(requestDate.value.toGregorianCalendar().getTime());
//            record.setValid(valid.value.toString());
//            record.setName(name.value);
//            record.setAddress(address.value);

            exchange.getIn().setBody(record);
        }
    }

    private class AggregateAsList implements AggregationStrategy {

        @Override
        public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
            List<VatRecord> recordList;
            if (oldExchange == null) {
                recordList = new ArrayList<>();
            } else {
                recordList = oldExchange.getIn().getBody(List.class);
            }

            recordList.add(newExchange.getIn().getBody(VatRecord.class));
            newExchange.getIn().setBody(recordList);
            return newExchange;
        }
    }

    private class CsvLineCount implements Expression {
        @Override
        public <T> T evaluate(Exchange exchange, Class<T> type) {
            long size = exchange.getIn().getBody(List.class).size();
            return type.cast(size);
        }
    }
}
