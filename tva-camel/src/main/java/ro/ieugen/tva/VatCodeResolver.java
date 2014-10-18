package ro.ieugen.tva;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Expression;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.cxf.common.message.CxfConstants;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import ro.ieugen.tva.csv.InvalidVatCodeFormat;
import ro.ieugen.tva.csv.VatCodeValidator;
import ro.ieugen.tva.csv.VatRecord;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class VatCodeResolver extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        BindyCsvDataFormat csvDataFormat = new BindyCsvDataFormat(VatRecord.class);

        onException(InvalidVatCodeFormat.class)
                .handled(true)
                .log("Exception validating VAT CODE ${in.headers.vatCode} - Ignored");

        from("{{vatcode.input}}")
                .routeId("VatCodeProcessor")
                .unmarshal(csvDataFormat)
                .setHeader(Headers.LIST_SIZE, new CsvLineCount())
                .split(simple("${in.body}"))
                        // make requests for each vat code
                .setHeader(Headers.VAT_CODE, new ValidaCodeFromVatRecord())
                .to("direct:make-single-vat-request")
                .aggregate(constant(true), new AggregateAsList())
                .completionSize(header(Headers.LIST_SIZE))
                .marshal(csvDataFormat)
                .to("{{vatcode.output}}");

        from("direct:make-single-vat-request")
                .process(new SoapRequestBuilder())
                .log("Processing ${in.headers.vatCode}")
                .to("{{vatcode.resolver}}")
                .process(new UpdateVatRecord());

    }

    public static class SoapRequestBuilder implements Processor {

        @Override
        public void process(Exchange exchange) throws Exception {
            String vatCode = exchange.getIn().getHeader(Headers.VAT_CODE, String.class);

            List<Object> params = new ArrayList<>();

            params.add("RO");
            params.add(vatCode);
            params.add(new Holder<Date>());
            params.add(new Holder<Boolean>());
            params.add(new Holder<String>());
            params.add(new Holder<String>());

            exchange.getIn().setHeader(CxfConstants.OPERATION_NAME, "checkVat");
            exchange.getIn().setBody(params);
        }
    }

    private static class ValidaCodeFromVatRecord implements Expression {

        private static final VatCodeValidator validator = new VatCodeValidator();

        @Override
        public <T> T evaluate(Exchange exchange, Class<T> type) {
            VatRecord record = exchange.getIn().getBody(VatRecord.class);
            // save the record
            exchange.getIn().setHeader(Headers.VAT_RECORD, record);

            return type.cast(validator.validateAndClean(record.getVatCode()));
        }
    }

    private class UpdateVatRecord implements Processor {

        @Override
        public void process(Exchange exchange) throws Exception {
            VatRecord record = exchange.getIn().getHeader(Headers.VAT_RECORD, VatRecord.class);

            List<Object> params = exchange.getIn().getBody(List.class);
            record.setRequestDate(getRequestDate(params));
            record.setValid(getValid(params));
            record.setName(getName(params));
            record.setAddress(getAddress(params));
            exchange.getIn().setBody(record);
        }

        private Date getRequestDate(List<Object> params) {
            Holder<XMLGregorianCalendar> date = (Holder<XMLGregorianCalendar>) params.get(3);
            return date.value.toGregorianCalendar().getTime();
        }

        private String getAddress(List<Object> params) {
            Holder<String> address = (Holder<String>) params.get(6);
            return address.value;
        }

        private String getName(List<Object> params) {
            Holder<String> name = (Holder<String>) params.get(5);
            return name.value;
        }

        private String getValid(List<Object> params) {
            Holder<Boolean> valid = (Holder<Boolean>) params.get(4);
            return valid.value.toString();
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
