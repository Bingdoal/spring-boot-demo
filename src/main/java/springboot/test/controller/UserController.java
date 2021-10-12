package springboot.test.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springboot.test.annotation.ApiPageable;
import springboot.test.dto.UserDto;
import springboot.test.dto.bean.I18nBean;
import springboot.test.dto.bean.PageResultBean;
import springboot.test.exception.StatusException;
import springboot.test.model.dao.UserDao;
import springboot.test.model.entity.User;
import springboot.test.service.UserDaoService;

import java.util.Optional;

@Slf4j
@RestController()
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserDaoService userDaoService;
    @Autowired
    private ObjectMapper objectMapper;

    @ApiPageable
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public PageResultBean<User> getAllUser(@QuerydslPredicate(root = User.class) Predicate predicate,
                                           final Pageable pageable) {
        return new PageResultBean<>(userDao.findAll(predicate, pageable));
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User getOneUser(@PathVariable("userId") Long userId) throws StatusException {
        Optional<User> userOption = userDao.findById(userId);
        if (userOption.isEmpty()) {
            throw new StatusException(400, new I18nBean("user.controller.not.found.by.id").args(userId));
        }
        return userOption.get();
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public JsonNode createUser(@RequestBody @Validated(UserDto.Create.class) UserDto userDco) {
        User user = userDaoService.create(userDco);
        return objectMapper.createObjectNode().put("id", user.getId());
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modifyUser(@PathVariable("userId") Long userId,
                           @RequestBody @Validated(UserDto.Update.class) UserDto userDuo) throws StatusException {
        userDaoService.modify(userId, userDuo);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUser(@PathVariable("userId") Long userId) {
        userDao.deleteById(userId);
    }
}
