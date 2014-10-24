package ro.ieugen.tva.web.services;

import lombok.extern.slf4j.Slf4j;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import ro.ieugen.tva.api.VatCodeService;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("/vat")
@Component(service = TvaFormResource.class, immediate = true)
@Slf4j
public class TvaFormResource {

    private VatCodeService vatCodeService;

    @Reference(unbind = "unbind")
    void bind(VatCodeService vatCodeService) {
        this.vatCodeService = vatCodeService;
    }

    void unbind(VatCodeService vatCodeService) {
        this.vatCodeService = null;
    }

    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("resolve")
    public Response processVatCodeRequest(@FormParam("email") String email,
                                          @FormParam("companyList") String companyList) throws IOException {

        log.info("Request for email {} received and awaits processing.", email);
        vatCodeService.resolveVatCodes(companyList, email);

        return Response.ok("{ \"message\": \"Request sent to process. You should receive a message later today.\" }").build();
    }
}

