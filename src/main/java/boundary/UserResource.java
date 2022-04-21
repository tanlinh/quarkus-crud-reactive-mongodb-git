package boundary;

import boundary.request.PageRequest;
import dto.UserDTO;
import entity.Address;
import entity.User;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import repository.UserRepository;
import serviceimpl.UserServiceImpl;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;
import java.util.Set;

@Slf4j
@RolesAllowed("ADMIN")
@Path("/user")
public class UserResource {

    @Inject
    UserServiceImpl userService;

    @Inject
    UserRepository userRepository;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<User> getAllUser() {
        return User.streamAll();
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> addUser(@Valid UserDTO userDTO) {
        return userService.addUser(userDTO);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> createUser(User user) {
        return user.<User>persist().map(v ->
                Response.created(URI.create("/user/" + v.id.toString()))
                        .entity(user).build());
    }

    @RolesAllowed({"USER", "ADMIN"})
    @PUT
    @Path("/update/{id}")
    public Uni<User> updateUser(@PathParam("id") String id, @Valid UserDTO userDTO) {
        return userService.updateUser(id, userDTO);
    }

    @PATCH
    @Path("/update-role/{id}")
    public Uni<User> update(@PathParam("id") String id, UserDTO userDTO) {
        return userService.updateRoleUser(id, userDTO);
    }

    @PUT
    @Path("/delete/{id}")
    public Uni<User> deleteUser(@PathParam("id") String id) {
        return userService.deleteUser(id);
    }

    @DELETE
    @Path("/{id}")
    public Uni<Boolean> delete(@PathParam("id") String id) {
        return User.deleteById(new ObjectId(id));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/search")
    public List<User> search(@DefaultValue("") @QueryParam("name") String name, @DefaultValue("") @QueryParam("email") String email) {
        return userRepository.find("{$and: [{name : {'$regex' : ?1, '$options' : 'i'}}, {email : {'$regex' : ?2, '$options' : 'i'} }]}", name, email).list();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/find-embedded")
    public List<User> findByProvince(@QueryParam("province") String province) {
        return userService.findByProvince(province);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/find-address")
    public Set<Address> findListAddress(@QueryParam("userName") String userName) {
        return userService.findListAddress(userName);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/paging")
    public Response getAll(@BeanParam PageRequest pageRequest) {
        return Response.ok(((PanacheMongoRepositoryBase) userRepository).findAll()
                .page(Page.of(pageRequest.getPageNum(), pageRequest.getPageSize()))
                .list()).build();
    }

}