package boundary;

import dto.UserDTO;
import dto.enumm.ERole;
import entity.Address;
import entity.User;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.security.TestSecurity;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.helpers.test.AssertSubscriber;
import io.smallrye.mutiny.helpers.test.UniAssertSubscriber;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import serviceimpl.UserServiceImpl;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;

import static io.restassured.RestAssured.given;
import static javax.ws.rs.core.HttpHeaders.ACCEPT;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.OK;

@Slf4j
@QuarkusTest
@Tag("integration")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserResourceTest {

//    @InjectMock
//    UserRepository userRepository;

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
        user.setStatus(true);
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
    public void testAdd() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("aaaaa");
        userDTO.setName("aaa");
        userDTO.setPassword("123");
        userDTO.setUserName("linh2a");
        userDTO.setPhoneNumber("012312312");
        userDTO.setStatus(true);
        Set<ERole> groups = new HashSet<>();
        groups.add(ERole.ADMIN);
        groups.add(ERole.USER);
        userDTO.setRoles(groups);
        given()
                .contentType(APPLICATION_JSON)
                .body(userDTO)
                .when().post("/user/add")
                .then()
                .statusCode(500);
    }

    @Test
    @TestSecurity(user = "test", roles = {"ADMIN", "USER"})
    public void testUpdate() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("aaaaa");
        userDTO.setName("aaa");
        userDTO.setPassword("123");
        userDTO.setUserName("linhvipp1ro");
        userDTO.setPhoneNumber("012312312");
        userDTO.setStatus(true);
        Set<ERole> groups = new HashSet<>();
        groups.add(ERole.ADMIN);
        groups.add(ERole.USER);
        userDTO.setRoles(groups);
        Address address = new Address();
        address.setDistrict("adsdasd");
        address.setProvince("asdasdas");
        address.setTown("Asdasds");
        Set<Address> addresses = new HashSet<>();
        addresses.add(address);
        userDTO.setAddresses(addresses);
        given()
                .contentType(APPLICATION_JSON)
                .pathParams("id", "625ce893cef88a5881b6d609").body(userDTO)
                .when().put("/user/update/{id}")
                .then()
                .statusCode(200);
    }

    @Test
    @TestSecurity(user = "test", roles = "ADMIN")
    public void testDelete() {
        given()
                .contentType(APPLICATION_JSON)
                .pathParams("id", "625d097171c77c0ad78f29ee")
                .when().put("/user/delete/{id}")
                .then()
                .statusCode(200);
    }

    @Test
    @TestSecurity(user = "test", roles = "ADMIN")
    public void testSearch() {
        given()
                .contentType(APPLICATION_JSON)
                .queryParam("email", "aaaaa")
                .queryParam("name", "aaa")
                .when().get("/user/search")
                .then()
                .statusCode(200);
    }

    @Test
    @TestSecurity(user = "a123", roles = {"ADMIN"} )
    public void testPaging() {
        given()
                .contentType(APPLICATION_JSON)
                .queryParam("pageNum", "0")
                .queryParam("pageSize", "10")
                .when().get("/user/paging")
                .then()
                .statusCode(200);
    }

    @Test
    @TestSecurity(user = "linhvippro", roles = "ADMIN")
    void delete() {
        Uni<Boolean> booleanUni = userResource.deleteAlways("6258d29a02604d69f5150b34");
        UniAssertSubscriber<Boolean> subscriber = booleanUni
                .subscribe().withSubscriber(UniAssertSubscriber.create());
        subscriber.assertSubscribed();
        booleanUni.subscribe().with(a-> Assertions.assertEquals(false, a.booleanValue()));
    }

    @Test
    @TestSecurity(user = "linhvippro", roles = {"ADMIN", "USER"})
    void updateUser() {
        UserDTO userDTO = new UserDTO();
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
