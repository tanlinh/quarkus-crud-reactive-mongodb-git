package serviceimpl;


import dto.UserDTO;
import dto.enumm.ERole;
import entity.Address;
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
import java.util.List;
import java.util.Set;


@RequestScoped
public class UserServiceImpl implements UserService {

    @Inject
    UserRepository userRepository;

    @Inject
    PasswordEncode passwordEncode;

    public Uni<Response> addUser(UserDTO userDTO) {
        User userExist = userRepository.findByUsername(userDTO.getUserName());
        if (userExist != null)
            throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Username is exist").build());
        Set<Address> addressDTOS = new HashSet<>();
        for (Address address : userDTO.getAddresses()) {
            addressDTOS.add(address);
        }
        User user = new User();
        user.setAddresses(addressDTOS);
        user.setEmail(userDTO.getEmail());
        user.setAddress(userDTO.getAddress());
        user.setName(userDTO.getName());
        user.setPhoneNumber(userDTO.getPhoneNumber());
        user.setStatus(true);
        user.setUserName(userDTO.getUserName());
        user.setFileUpload(userDTO.getFile());
        Set<ERole> groups = new HashSet<>();
        for (ERole role : userDTO.getRoles()) {
            if (role.equals(ERole.ADMIN))
                throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Cannot add user with role admin").build());
            groups.add(role);
        }
        user.setRoles(groups);
        user.setPassword(passwordEncode.encode(userDTO.getPassword()));
        return user.persist().map(m -> Response.created(URI.create("/user/")).entity(userDTO).status(200).build());
    }

    public Uni<User> updateUser(String id, UserDTO userDTO) {
        Uni<User> userUni = User.findById(new ObjectId(id));
        Set<Address> addressDTOS = new HashSet<>();
        return userUni.onItem().transform(Unchecked.function(updateUser -> {
            if (updateUser == null)
                throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("User does not exist").build());
            updateUser.setName(userDTO.getName() == null ? updateUser.getName() : userDTO.getName());
            updateUser.setAddress(userDTO.getAddress() == null ? updateUser.getAddress() : userDTO.getAddress());
            updateUser.setEmail(userDTO.getEmail() == null ? updateUser.getEmail() : userDTO.getEmail());
            updateUser.setPhoneNumber(userDTO.getPhoneNumber() == null ? updateUser.getPhoneNumber() : userDTO.getPhoneNumber());
            updateUser.setPassword(userDTO.getPassword() == null ? updateUser.getPassword() : passwordEncode.encode(userDTO.getPassword()));
            for (Address address : userDTO.getAddresses()) {
                addressDTOS.add(address);
            }
            updateUser.setAddresses(updateUser.getAddresses() == null ? updateUser.getAddresses() : addressDTOS);
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

        User user = userRepository.findByUsername(userName);
        if (user == null)
            throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Username does not exist").build());
        return userRepository.findByUsername(userName);
    }

    public List<User> findByProvince(String province) {
        List<User> users = userRepository.findByProvince(province);
        if (users.isEmpty())
            throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("User not found").build());
        return users;
    }

    public Set<Address> findListAddress(String userName) {
        User user = userRepository.findByUsername(userName);
        if (user == null)
            throw new WebApplicationException(Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Username does not exist").build());
        return user.getAddresses();
    }
}
