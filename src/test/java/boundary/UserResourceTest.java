package boundary;

import dto.UserDTO;
import dto.enumm.ERole;
import entity.User;
import io.quarkus.mongodb.panache.reactive.ReactivePanacheMongoEntityBase;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.security.TestSecurity;
import io.smallrye.common.constraint.Assert;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import repository.UserRepository;
import serviceimpl.UserServiceImpl;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;

@QuarkusTest
public class UserResourceTest {

    @InjectMock
    UserRepository userRepository;

    @Inject
    UserServiceImpl userService;

    @Inject
    UserResource userResource;

    private User user;

    @BeforeEach
    void setup() {
        this.user = new User();
        user.setName("linhvippro");
        user.setUserName("linhvippro00");
        userService = Mockito.mock(UserServiceImpl.class);
//        reactivePanacheMongoEntityBaseUni = User.findById(new ObjectId("624e495505042b1dfb7aea73"));
//        reactivePanacheMongoEntityBaseUni.onItem().transform(a-> a= user);
    }

    @Test()
    @TestSecurity(user = "linhvippro", roles = "ADMIN")
    void getAll() {
        Multi<User> userMulti = userResource.getAllUser();
        AssertSubscriber<User> subscriber = userMulti.subscribe().withSubscriber(AssertSubscriber.create());
        subscriber.onComplete();
    }

    @Test
    @TestSecurity(user = "linhvippro", roles = "ADMIN")
    void addUser() {
        Mockito.when(userRepository.findByName("linhvippro00")).thenReturn(user);
        UserDTO userDTO = new UserDTO();
        userDTO.setAddress("aa");
        userDTO.setEmail("aaaaa");
        userDTO.setName("aaa");
        userDTO.setPassword("123");
        userDTO.setUserName("linhvippro");
        userDTO.setPhoneNumber("012312312");
        userDTO.setStatus(true);
        Set<ERole> groups = new HashSet<>();
        groups.add(ERole.ADMIN);
        groups.add(ERole.USER);
        userDTO.setRoles(groups);
        Uni<Response> responseUser = userResource.addUser(userDTO);
        responseUser.onItem().transform(Assert::assertNotNull);
    }

    @Test
    @TestSecurity(user = "linhvippro", roles = "ADMIN")
    void delete() {
        Uni<Boolean> booleanUni = userResource.delete("624ffc456ad6787062128435");
        UniAssertSubscriber<Boolean> subscriber = booleanUni
                .subscribe().withSubscriber(UniAssertSubscriber.create());
        subscriber.assertSubscribed();
    }

    @Test
    @TestSecurity(user = "linhvippro", roles = {"ADMIN", "USER"})
    void updateUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setAddress("aa");
        userDTO.setEmail("aaaaa");
        userDTO.setName("aaa");
        userDTO.setPassword("123");
        userDTO.setPhoneNumber("012312312");
        Uni<User> userUni = userResource.updateUser("624e495505042b1dfb7aea73", userDTO);
        UniAssertSubscriber<User> subscriber = userUni
                .subscribe().withSubscriber(UniAssertSubscriber.create());
        subscriber.assertSubscribed();
    }

}
