package ro.ieugen.tva;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import ro.ieugen.tva.api.Headers;

import java.util.List;
import java.util.Map;

@Slf4j
@Component(immediate = true)
public class EmailReporting extends RouteBuilder {

    private MailService mailService;

    @Activate
    void activate(Map<String, Object> config) throws Exception {
        this.mailService = new MailService(config);
    }

    @Override
    public void configure() throws Exception {

        from("{{vatcode.output}}")
                .routeId("Email_reporting_route")
                .log("Sending email ${in.headers.tvaEmail}")
                .process(sendEmailReport());

    }

    private Processor sendEmailReport() {
        return new Processor() {
            @Override
            public void process(Exchange exchange) throws Exception {
                log.info("Sending email");
                String destination = exchange.getIn().getHeader(Headers.EMAIL, String.class);

                mailService.sendEmail(destination, exchange.getIn().getBody(List.class));
            }
        };
    }
}
