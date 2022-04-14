package boundary;

import boundary.request.PageRequest;
import dto.UserDTO;
import dto.enumm.ERole;
import entity.User;
import io.quarkus.mongodb.panache.PanacheQuery;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import repository.UserRepository;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.smallrye.common.constraint.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class UserResourceTest {

    @InjectMock
    UserRepository userRepository;

    @Inject
    UserResource userResource;

    private User user;

    @BeforeEach
    void setup() {
        user = new User();
        user.setName("linhvippro");
    }

    @Test()
    void getALl() {
        Multi<User> userMulti = userResource.getAllUser();
        assertNotNull(userMulti);
    }

    @Test
    void addUser() {
        Mockito.when(userRepository.findByName("linhvippro")).thenReturn(user);
        UserDTO userDTO = new UserDTO();
        userDTO.setAddress("aa");
        userDTO.setEmail("aaaaa");
        userDTO.setName("aaa");
        userDTO.setPassword("123");
        userDTO.setPhoneNumber("012312312");
        userDTO.setStatus(true);
        Set<ERole> groups = new HashSet<>();
        groups.add(ERole.ADMIN);
        groups.add(ERole.USER);
        userDTO.setRoles(groups);
        Uni<Response> responseUser = userResource.addUser(userDTO);
        assertNotNull(responseUser);
        assertNotNull(responseUser.onItem().transform(a -> a.getEntity()));
    }


    @Test
    void delete() {
        Uni<Boolean> userUni = userResource.delete("");
        assertEquals(true, userUni);
    }

    @Test
    void getAllPaging() {
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPageNum(0);
        pageRequest.setPageSize(1);
        List<User> list = new ArrayList<>();
        Mockito.when(userRepository.findAll()).thenReturn((PanacheQuery<User>) list);
        Response response = userResource.getAll(pageRequest);
        assertNotNull(response);
        assertEquals(Response.status(200), response.getStatus());
    }
}
