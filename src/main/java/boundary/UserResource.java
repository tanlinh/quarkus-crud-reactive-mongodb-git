package boundary;

import boundary.request.PageRequest;
import dto.UserDTO;
import entity.User;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.bson.types.ObjectId;
import repository.UserRepository;
import serviceimpl.UserServiceImpl;

import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

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

    @Transactional
    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> addUser(@Valid UserDTO userDTO) {
        return userService.addUser(userDTO);
    }

    @Transactional
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> createUser(User user) {
        return user.<User>persist().map(v ->
                Response.created(URI.create("/user/" + v.id.toString()))
                        .entity(user).build());
    }

    @Transactional
    @RolesAllowed("USER")
    @PUT
    @Path("/update/{id}")
    public Uni<User> updateUser(@PathParam("id") String id, UserDTO userDTO) {
        return userService.updateUser(id, userDTO);
    }

    @Transactional
    @PATCH
    @Path("/update/{id}")
    public Uni<User> update(@PathParam("id") String id, UserDTO userDTO) {
        return userService.updateRoleUser(id, userDTO);
    }

    @Transactional
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
    public List<User> search(@QueryParam("name") String name, @QueryParam("email") String email) {
        return userRepository.find("{$and: [{name: ?1},{email: ?2}]}", name, email).list();//"{name = ?1, email : ?2}"
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