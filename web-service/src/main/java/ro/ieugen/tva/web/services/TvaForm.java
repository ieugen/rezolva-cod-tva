package ro.ieugen.tva.web.services;

import lombok.extern.slf4j.Slf4j;
import org.osgi.service.component.annotations.Component;
import ro.ieugen.tva.web.model.CompanyRecord;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Path("/vat")
@Component(service = TvaForm.class, immediate = true)
@Slf4j
public class TvaForm {

    final Map<String, String> config;
    final MailService mailService;

    public TvaForm() {
        this(System.getenv(), new MailService(System.getenv()));
    }

    public TvaForm(Map<String, String> config, MailService sender) {
        this.config = config;
        this.mailService = sender;
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/resolve")
    public Response processVatCodeRequest(@FormParam("email") String email,
                                          @FormParam("companyList") String companyList) throws IOException {

        log.debug("Available configuration: {}", config);
        log.info("Request for email {} received and awaits processing.", email);

        List<CompanyRecord> records = fetchCompanyVatInformation();
        GenericEntity<List<CompanyRecord>> genericEntity = new GenericEntity<List<CompanyRecord>>(records) {
        };
        mailService.sendEmail(email, records);

        return Response.ok(genericEntity).build();
    }

    private List<CompanyRecord> fetchCompanyVatInformation() {
        List<CompanyRecord> records = new ArrayList<>();
        records.add(new CompanyRecord("A SRL", "RO112233", "true"));
        records.add(new CompanyRecord("B SRL", "RO112244", "false"));
        records.add(new CompanyRecord("C SRL", "RO112255", "true"));
        return records;
    }

    public static String readInputStreamAsString(InputStream in) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(in);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        int result = bis.read();
        while (result != -1) {
            byte b = (byte) result;
            buf.write(b);
            result = bis.read();
        }
        return buf.toString();
    }

}

