package ro.ieugen.tva;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultExchange;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import ro.ieugen.tva.api.Headers;
import ro.ieugen.tva.api.VatCodeService;

@Component(immediate = true, service = VatCodeService.class)
public class VatCodeServiceImpl implements VatCodeService {

    private ProducerTemplate producerTemplate;
    private CamelContext camelContext;

    @Reference
    void bind(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    @Activate
    void activate() throws Exception {
        producerTemplate = camelContext.createProducerTemplate();
    }

    @Deactivate
    void deactivate() throws Exception {
        producerTemplate.stop();
    }

    public void resolveVatCodes(String csvVatCodeList, String email) {
        Exchange exchange = new DefaultExchange(camelContext);
        exchange.getIn().setBody(csvVatCodeList);
        exchange.getIn().setHeader(Headers.EMAIL, email);

        producerTemplate.send("{{vatcode.input}}", exchange);
    }
}
