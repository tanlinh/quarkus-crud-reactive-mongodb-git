package boundary;

import dto.UserDTO;
import dto.enumm.ERole;
import entity.User;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.bson.types.ObjectId;
import repository.UserRepository;
import serviceimpl.UserServiceImpl;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.Collections;
import java.util.List;

@Path("/user")
public class UserResource {

    @Inject
    UserServiceImpl userService;

    @Inject
    UserRepository userRepository;

    @RolesAllowed("USER")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<User> getAllUser() {
        return User.streamAll();
    }

    @Transactional
    @POST
//    @RolesAllowed("USER")
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> addUser(UserDTO userDTO) {
        //check userName
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setAddress(userDTO.getAddress());
        user.setName(userDTO.getName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setStatus(true);
        user.setUserName(userDTO.getUserName());
        user.setRoles(Collections.singleton(ERole.USER));
//        user.setPassword(BcryptUtil.bcryptHash(userDTO.getPassword()));
        user.setPassword(userDTO.getPassword());
        return user.persist().map(m -> Response.created(URI.create("/user/")).entity(user).build());
    }

    @Transactional
    @POST
    @RolesAllowed("ADMIN")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> createUser(User user) {
        return user.<User>persist().map(v ->
                Response.created(URI.create("/user/" + v.id.toString()))
                        .entity(user).build());
    }

    @Transactional
    @PUT
    @RolesAllowed({"USER", "ADMIN"})
    @Path("/update/{id}")
    public Uni<User> updateUser(@PathParam("id") String id, UserDTO userDTO) {
        return userService.updateUser(id, userDTO);
    }

    @Transactional
    @PUT
    @RolesAllowed("ADMIN")
    @Path("/delete/{id}")
    public Uni<User> deleteUser(@PathParam("id") String id) {
        return userService.deleteUser(id);
    }

    @DELETE
    @RolesAllowed("ADMIN")
    @Path("/{id}")
    public Uni<Boolean> delete(@PathParam("id") String id) {
        return User.deleteById(new ObjectId(id));
    }


    @GET
    @RolesAllowed("ADMIN")
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/search")
    public List<User> search(@QueryParam("name") String name, @QueryParam("email") String email) {
        return userRepository.find("{name : ?1, email : ?2}", name, email).list();
    }


//    @GET
//    @Path("/paging")
//    @Produces(MediaType.APPLICATION_JSON)
//    public List<User> getAllUserWithPaging(String sortKey, int pageSize, int pageOffset) {
//        return userRepository.findAll(Sort.by(sortKey)).page(pageOffset,pageSize).stream().collect(Collectors.toList());
//    }
}