package processor;

import dto.UserDTO;
import entity.User;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserProcessor {

    public UserDTO userProcessor(UserDTO user) {

        String test = user.getName().concat("testtt");
        user.setName(test);
        return user;
    }
}
