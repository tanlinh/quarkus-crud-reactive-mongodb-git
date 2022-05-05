package boundary;

import entity.Address;
import io.smallrye.mutiny.Uni;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/api/address")
@RolesAllowed("ADMIN")
public class AddressResource {

    @POST
    @Path("/add")
    public Uni<Response> addAddress(Address address) {
        return address.persist().map(v-> Response.created(URI.create("/api/address")).entity(address).build());
    }
}
