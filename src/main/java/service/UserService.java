package service;

import entity.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {

    User findByUsername(String userName);
}
