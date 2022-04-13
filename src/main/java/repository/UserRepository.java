package repository;

import entity.User;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheMongoRepositoryBase<User, String> {

    public User findByName(String userName){
        return find("userName", userName).firstResult();
    }

}
