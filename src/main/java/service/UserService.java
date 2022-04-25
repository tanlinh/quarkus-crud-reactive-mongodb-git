package service;

import entity.User;

public interface UserService {

    User findByUsername(String userName);
}
