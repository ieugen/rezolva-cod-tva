package ro.ieugen.tva;

import org.apache.camel.builder.RouteBuilder;

public class VatCodeListResolver extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        from("{{vatcode.input}}")
                .routeId("VatCodeProcessor")
                .log("will split file ${in.body}")
                .split(new SplitVatCodeList())
                .log("body is ${in.body}")
                .to("{{vatcode.output}}");
    }
}
