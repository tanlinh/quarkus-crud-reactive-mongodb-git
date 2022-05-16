package service;

import dto.UserDTO;
import entity.Address;
import entity.Order;
import entity.User;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;


public interface UserService {

    User findByUsername(String userName);

    Uni<Response> addUser(UserDTO userDTO);

    Uni<User> updateUser(String id, UserDTO userDTO);

    Uni<User> deleteUser(String id);

    Uni<User> updateRoleUser(String id, UserDTO userDTO);

    List<User> findByProvince(String province);

    Uni<List<Order>> findListOrder(String id);

}
