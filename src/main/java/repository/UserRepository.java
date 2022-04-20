package repository;

import entity.User;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class UserRepository implements PanacheMongoRepositoryBase<User, String> {

    public User findByName(String userName) {
        return find("userName", userName).firstResult();
    }

    public List<User> findUserWithAddress(String province) {
        return find("addresses.province", province).list();
    }
}
