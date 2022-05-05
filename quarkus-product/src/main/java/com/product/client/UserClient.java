package com.product.client;

import com.product.dto.UserDTO;
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

@RegisterRestClient
@Path("/user")
public interface UserClient {

    @GET
    @Path("/get/{id}")
    UserDTO findUser(@PathParam("id") String id);

}
