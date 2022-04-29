package service;

import entity.User;
import repository.UserRepository;
import serviceimpl.UserServiceImpl;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.io.ByteArrayInputStream;
import java.util.List;

@RequestScoped
public class ExcelService {

    @Inject
    UserRepository userRepository;

    @Inject
    UserServiceImpl userService;
    public ByteArrayInputStream load() {
//        Multi<User> userMulti = User.streamAll();
        List<User> userList = userService.findByProvince("Tphcm1");
        ByteArrayInputStream in = ExcelHelper.tutorialsToExcel(userList);
        return in;
    }
}
