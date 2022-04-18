package boundary;

import dto.UserDTO;
import dto.enumm.ERole;
import entity.User;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import io.quarkus.test.security.TestSecurity;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import repository.UserRepository;
import serviceimpl.UserServiceImpl;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.HashSet;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.OK;

@Slf4j
@QuarkusTest
//@TestHTTPEndpoint(UserResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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
        userResource = Mockito.mock(UserResource.class);
        user = Mockito.mock(User.class);
        this.user = new User();
        user.setName("linhvippro");
        user.setUserName("linhvippro00");
        userService = Mockito.mock(UserServiceImpl.class);
    }

    @Test
    @TestSecurity(user = "l123", roles = {"ADMIN", "USER"})
    void test() {
        given()
                .header(ACCEPT, APPLICATION_JSON)
                .when().get("/user")
                .then()
                .statusCode(OK.getStatusCode());
    }


    @Test
    @TestSecurity(user = "test", roles = "ADMIN")
    public void testSearch() {
        given()
                .header(ACCEPT, APPLICATION_JSON)
                .when().get("/user/search?email=tes1t@gmail.com")
                .then()
                .statusCode(500);
    }

    @Test
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
        UniAssertSubscriber<Response> subscriber = responseUser
                .subscribe().withSubscriber(UniAssertSubscriber.create());
        subscriber.assertSubscribed().assertCompleted();
    }

    @Test
    @TestSecurity(user = "linhvippro", roles = "ADMIN")
    void delete() {
        Uni<Boolean> booleanUni = userResource.delete("6258d29a02604d69f5150b34");
        UniAssertSubscriber<Boolean> subscriber = booleanUni
                .subscribe().withSubscriber(UniAssertSubscriber.create());
        subscriber.assertSubscribed();
        booleanUni.subscribe().with(a-> Assertions.assertEquals(false, a.booleanValue()));
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
        subscriber.assertSubscribed().assertCompleted();
    }

}
