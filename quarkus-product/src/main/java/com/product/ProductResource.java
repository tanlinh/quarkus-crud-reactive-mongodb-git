package com.product;

import com.product.client.UserClient;
import com.product.dto.ProductDTO;
import com.product.dto.UserDTO;
import com.product.entity.Product;
import com.product.mapper.ProductMapper;
import com.product.repository.ProductRepository;
import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

//@Authenticated
@RolesAllowed("ADMIN")
@Path("/product")
@ApplicationScoped
public class ProductResource {

    @Inject
    ProductRepository productRepository;

    @Inject
    ProductMapper productMapper;

    @RestClient
    @Inject
    UserClient userClient;

    @Path("/get-all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductDTO> getAllProduct() {
        return productRepository.listAll().stream().map(productMapper::toDTO).collect(Collectors.toList());
    }

    @Path("/get-product")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductDTO> listProductByName(@QueryParam("productName") String productName) {
       return productRepository.find("productList.productName", productName).list().stream().map(productMapper ::toDTO).collect(Collectors.toList());
    }

    @Path("/add")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> createUser(Product product) {
        return product.<Product>persist().map(v ->
                Response.ok()
                        .entity(product).build());
    }

    @Path("/product-user/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProductDTO> listProductByUser(@PathParam("id") String id) {
        UserDTO userDTO = userClient.findUser(id);
        return userDTO.getProductList();
    }
}