package processor;

import entity.User;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserProcessor {

    public User userProcessor(User user) {

        String test = user.getName().concat("testtt");
        user.setName(test);
        return user;
    }
}
