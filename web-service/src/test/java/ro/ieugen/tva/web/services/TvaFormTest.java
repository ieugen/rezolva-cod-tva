package ro.ieugen.tva.web.services;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.moxy.json.MoxyJsonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Assert;
import org.junit.Test;
import ro.ieugen.tva.web.model.CompanyRecord;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;

public class TvaFormTest extends JerseyTest {

    @Override
    protected Application configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);

        ResourceConfig config = new ResourceConfig();
        config.registerInstances(new TvaForm(MailServiceLiveTest.createFaceEnv(), mock(MailService.class)));
        config.register(MoxyJsonFeature.class);
        return config;
    }

    @Override
    protected void configureClient(ClientConfig config) {
        config.register(MoxyJsonFeature.class);
    }

    @Test
    public void testTvaRecord() throws Exception {
        MultivaluedMap<String, String> vatForm = new MultivaluedHashMap();
        vatForm.add("email", "stan.ieugen@gmail.com");
        vatForm.add("companyList", "aaaalaaallala");

        List<CompanyRecord> resonse = target("vat/resolve").request()
                .post(Entity.form(vatForm), new GenericType<List<CompanyRecord>>() {
                });

        Assert.assertThat(resonse.size(), is(3));
    }
}