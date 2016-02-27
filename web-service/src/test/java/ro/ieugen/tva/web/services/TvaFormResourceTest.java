package ro.ieugen.tva.web.services;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Test;
import ro.ieugen.tva.api.VatCodeService;
import ro.ieugen.tva.api.model.VatRequest;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.mock;

public class TvaFormResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);

        ResourceConfig config = new ResourceConfig();
        TvaFormResource formResource = new TvaFormResource();
        formResource.bind(mock(VatCodeService.class));

        config.registerInstances(formResource);
        config.register(MoxyJsonFeature.class);
        return config;
    }

    @Override
    protected void configureClient(ClientConfig config) {
        config.register(MoxyJsonFeature.class);
    }

    @Test
    public void testTvaRecord() throws Exception {
        VatRequest vatRequest = new VatRequest("stan.ieugen@gmail.com", "aaa;ro221212");
        String resonse = target("vat/resolve").request()
                .post(Entity.entity(vatRequest, MediaType.APPLICATION_JSON_TYPE), String.class);

        assertThat(resonse, containsString("Request sent to process"));
    }
}