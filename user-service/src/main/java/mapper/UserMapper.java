package mapper;

import dto.UserDTO;
import entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "cdi")
public interface UserMapper extends EntityMapper<UserDTO, User> {

    UserDTO toDTO (User user);
}
