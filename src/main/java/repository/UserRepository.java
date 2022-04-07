package repository;

import entity.User;
import io.quarkus.mongodb.panache.PanacheMongoRepositoryBase;
import service.UserService;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class UserRepository implements PanacheMongoRepositoryBase<User, String> {


}
