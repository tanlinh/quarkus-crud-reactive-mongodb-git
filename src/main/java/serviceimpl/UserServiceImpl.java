package serviceimpl;


import dto.UserDTO;
import entity.User;
import io.smallrye.mutiny.Uni;
import org.bson.types.ObjectId;
import repository.UserRepository;
import service.UserService;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.NotFoundException;
import java.util.List;

@RequestScoped
public class UserServiceImpl implements UserService {

    @Inject
    UserRepository userRepository;

    public Uni<User> updateUser(String id, UserDTO userDTO) {
        Uni<User> userUni = User.findById(new ObjectId(id));
        userUni.onFailure();
        return userUni.onItem().transform(updateUser -> {
            updateUser.setName(userDTO.getName());
            updateUser.setAddress(userDTO.getAddress());
            updateUser.setEmail(userDTO.getEmail());
            updateUser.setPhoneNumber(userDTO.getPhoneNumber());
            updateUser.setRoles(userDTO.getRoles());
            return updateUser;
        }).call(updateUser -> updateUser.update()).onFailure().recoverWithNull();
    }

    public Uni<User> deleteUser(String id) {
        Uni<User> uni = User.findById(new ObjectId(id));
        return uni.onItem().transform(deleteUser -> {
            deleteUser.setStatus(false);
            return deleteUser;
        }).call(delete -> delete.update());
    }

    public User findByUsername(String userName) {

        List<User> users = userRepository.find("{userName : ?1}", userName).list();

        return users.stream().filter(m-> userName.equals(m.getUserName())).findAny().orElseThrow(NotFoundException::new);
    }
}
