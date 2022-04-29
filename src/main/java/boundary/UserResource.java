package boundary;

import boundary.request.PageRequest;
import dto.UserDTO;
import entity.Address;
import entity.User;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import io.quarkus.panache.common.Page;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import lombok.extern.slf4j.Slf4j;
import mapper.UserMapper;
import org.bson.types.ObjectId;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import processor.UserProcessor;
import repository.UserRepository;
import service.ExcelService;
import service.FileUploadService;
import service.UserService;

import javax.annotation.security.RolesAllowed;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.List;
import java.util.Set;

@Slf4j
//@RolesAllowed("ADMIN")
@Path("/user")
@ApplicationScoped
public class UserResource {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserResource.class);

    @Inject
    UserService userService;

    @Inject
    UserRepository userRepository;

    @Inject
    ExcelService excelService;

    @Inject
    FileUploadService uploadService;

    @Inject
    UserProcessor userProcessor;

    @Inject
    UserMapper userMapper;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Multi<UserDTO> getAllUser() {
        return User.streamAll().map(item -> userMapper.toDTO((User) item));
    }

    @Path("/one")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public UserDTO getUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserName("asdsadasd");
        userDTO.setAddress("asdassa");
        return userDTO;
    }

    @POST
    @Path("/add")
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> addUser(@Valid UserDTO userDTO) {
        return userService.addUser(userDTO);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Uni<Response> createUser(User user) {
        return user.<User>persist().map(v ->
                Response.created(URI.create("/user/" + v.id.toString()))
                        .entity(user).build());
    }

    @RolesAllowed({"USER", "ADMIN"})
    @PUT
    @Path("/update/{id}")
    public Uni<User> updateUser(@PathParam("id") String id, @Valid UserDTO userDTO) {
        return userService.updateUser(id, userDTO);
    }

    @PATCH
    @Path("/update-role/{id}")
    public Uni<User> updateRole(@PathParam("id") String id, UserDTO userDTO) {
        return userService.updateRoleUser(id, userDTO);
    }

    @PUT
    @Path("/delete/{id}")
    public Uni<User> deleteUser(@PathParam("id") String id) {
        return userService.deleteUser(id);
    }

    @DELETE
    @Path("/{id}")
    public Uni<Boolean> deleteAlways(@PathParam("id") String id) {
        return User.deleteById(new ObjectId(id));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/search")
    public List<User> searchByNameAndEmail(@DefaultValue("") @QueryParam("name") String name, @DefaultValue("") @QueryParam("email") String email) {
        return userRepository.find("{$and: [{name : {'$regex' : ?1, '$options' : 'i'}}, {email : {'$regex' : ?2, '$options' : 'i'} }]}", name, email).list();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/find-embedded")
    public List<User> findByProvince(@QueryParam("province") String province) {
        return userService.findByProvince(province);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/find-address")
    public Set<Address> findListAddress(@QueryParam("userName") String userName) {
        return userService.findListAddress(userName);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/paging")
    public Response getAllByPaging(@BeanParam PageRequest pageRequest) {
        return Response.ok(((PanacheMongoRepositoryBase) userRepository).findAll()
                .page(Page.of(pageRequest.getPageNum(), pageRequest.getPageSize()))
                .list()).build();
    }

    @POST
    @Path("/upload-file")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    public Response sendMultipartUpLoadFile(@MultipartForm MultipartFormDataInput input) {
        return Response.ok().entity(uploadService.uploadFile(input)).build();
    }

    @GET
    @Produces("application/vnd.ms-excel")
    @Path("/download-file")
    public Response downloadFileExcel() {
        LOGGER.debug("Check download file excel of list User");
        String filename = "UserList.xlsx";
        ByteArrayInputStream file = excelService.load();
        return Response.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filename).entity(file).build();
    }

    @Inject
    @Channel("user-out")
    Emitter<User> userEmitter;


    @Path("/kafka")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response userKafka(User user) {
        user.setName("asdasdsa");
        user = userProcessor.userProcessor(user);
        userEmitter.send(user);
        return Response.ok().build();
    }


}