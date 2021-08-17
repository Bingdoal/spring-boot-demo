package springboot.test.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springboot.test.dto.PageResultDto;
import springboot.test.dto.user.UserDco;
import springboot.test.model.dao.UserDao;
import springboot.test.model.entity.User;

import javax.validation.Valid;

@Slf4j
@RestController()
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    private UserDao userDao;
    @Autowired
    private ObjectMapper objectMapper;

    @GetMapping("/page")
    @ResponseStatus(HttpStatus.OK)
    public Page<User> getAllUserPage(@QuerydslPredicate(root = User.class) Predicate predicate,
                                     final Pageable pageable) {
        return userDao.findAll(predicate, pageable);
    }

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public PageResultDto<User> getAllUser(@QuerydslPredicate(root = User.class) Predicate predicate,
                                          final Pageable pageable) {
        return new PageResultDto<>(userDao.findAll(predicate, pageable));
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public void createUser(@RequestBody @Valid UserDco userDco) {
        User user = userDco.toUser();
        userDao.saveAndFlush(user);
    }
}
