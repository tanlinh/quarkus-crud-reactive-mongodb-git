package service;

import entity.User;

@Deprecated
public interface UserService {

    User findByUsername(String userName);
}
