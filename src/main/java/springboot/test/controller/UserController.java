package springboot.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springboot.test.model.UserEntity;

import java.util.ArrayList;
import java.util.List;

@RestController()
@RequestMapping("/v1/user")
public class UserController {

    @GetMapping("")
    public List<UserEntity> getAllUser() {
        List<UserEntity> userEntities = new ArrayList<>();
        return userEntities;
    }
}
