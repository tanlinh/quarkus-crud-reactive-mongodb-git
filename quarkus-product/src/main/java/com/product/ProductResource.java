package com.product;

import com.product.client.UserClient;
import com.product.dto.ProductDTO;
import com.product.dto.UserDTO;
import com.product.entity.Product;
import com.product.entity.User;
import com.product.mapper.ProductMapper;
import com.product.repository.ProductRepository;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

//@Authenticated
@Slf4j
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
       return productRepository.find("{productName: {'$regex': ?1}}", productName).list().stream().map(productMapper ::toDTO).collect(Collectors.toList());
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

    @Incoming("user-in")
    public void userIn(User user) {
        System.out.printf("data test:  %s ", user.getUserName());
    }
}