package client;

import dto.ProductDTO;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import java.util.List;

@RegisterRestClient
@Path("/product")
public interface ProductClient {

    @GET
    @Path("/get-all")
    List<ProductDTO> listProduct();

    @GET
    @Path("/get-product")
    List<ProductDTO> getProduct(@QueryParam("productName") String productName);
}
