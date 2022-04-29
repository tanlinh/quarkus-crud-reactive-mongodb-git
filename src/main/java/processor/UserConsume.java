package processor;

import entity.User;
import org.eclipse.microprofile.reactive.messaging.Incoming;

public class UserConsume {

    @Incoming("user-in")
    public void userIn(User user) {
        System.out.printf("ra day nha  %s ", user.getName());
    }
}
