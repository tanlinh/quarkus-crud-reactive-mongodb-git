package serviceimpl;


import dto.UserDTO;
import dto.enumm.ERole;
import entity.User;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.unchecked.Unchecked;
import org.bson.types.ObjectId;
import repository.UserRepository;
import security.PasswordEncode;
import service.UserService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

@RequestScoped
public class UserServiceImpl implements UserService {

    @Inject
    UserRepository userRepository;

    @Inject
    PasswordEncode passwordEncode;

    public Uni<Response> addUser(UserDTO userDTO) {
        User userExist = userRepository.findByName(userDTO.getUserName());
        if (userExist != null)
            throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Username is exist").build());
        User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setAddress(userDTO.getAddress());
        user.setName(userDTO.getName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setStatus(true);
        user.setUserName(userDTO.getUserName());
        Set<ERole> groups = new HashSet<>();
        for (ERole role : userDTO.getRoles()) {
            groups.add(role);
        }
        user.setRoles(groups);
        user.setPassword(passwordEncode.encode(userDTO.getPassword()));
        return user.persist().map(m -> Response.created(URI.create("/user/")).entity(user).build());
    }

    public Uni<User> updateUser(String id, UserDTO userDTO) {
        Uni<User> userUni = User.findById(new ObjectId(id));
        return userUni.onItem().transform(Unchecked.function(updateUser -> {
            if (updateUser == null)
                throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("User does not exist").build());
            updateUser.setName(userDTO.getName());
            updateUser.setAddress(userDTO.getAddress());
            updateUser.setEmail(userDTO.getEmail());
            updateUser.setPhoneNumber(userDTO.getPhoneNumber());
            updateUser.setPassword(passwordEncode.encode(userDTO.getPassword()));
            return updateUser;
        })).call(updateUser -> updateUser.update());
    }

    public Uni<User> deleteUser(String id) {
        Uni<User> uni = User.findById(new ObjectId(id));
        return uni.onItem().transform(Unchecked.function(deleteUser -> {
            if (deleteUser == null)
                throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("User does not exist").build());
            deleteUser.setStatus(false);
            return deleteUser;
        })).call(delete -> delete.update());
    }

    public Uni<User> updateRoleUser(String id, UserDTO userDTO) {
        Uni<User> uni = User.findById(new ObjectId(id));
        Set<ERole> groups = new HashSet<>();
        for (ERole role : userDTO.getRoles()) groups.add(role);
        return uni.onItem().transform(Unchecked.function(updateRole -> {
            if (updateRole == null)
                throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("User does not exist").build());
            updateRole.setRoles(groups);
            return updateRole;
        })).call(updateRole -> updateRole.update());
    }

    public User findByUsername(String userName) {

//        List<User> users = userRepository.find("{userName : ?1}", userName).list();
//        return users.stream().filter(m -> userName.equals(m.getUserName())).findAny().orElse(null);//.orElseThrow(WebApplicationException::new);
        User user = userRepository.findByName(userName);
        if (user == null)
            throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Username does not exist").build());
        return userRepository.findByName(userName);
    }
}
