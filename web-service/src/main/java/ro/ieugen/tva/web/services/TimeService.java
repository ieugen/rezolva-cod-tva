package ro.ieugen.tva.web.services;

import org.osgi.service.component.annotations.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/time")
@Produces(MediaType.APPLICATION_JSON)
@Component(service = TimeService.class, immediate = true)
public class TimeService {

    @GET
    public String get() {
        return String.valueOf(System.currentTimeMillis());
    }

}

