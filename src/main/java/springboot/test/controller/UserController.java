package springboot.test.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.core.types.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import springboot.test.dto.PageResultDto;
import springboot.test.dto.user.UserDco;
import springboot.test.dto.user.UserDro;
import springboot.test.dto.user.UserDuo;
import springboot.test.exception.StatusException;
import springboot.test.model.dao.UserDao;
import springboot.test.model.entity.User;
import springboot.test.service.UserDaoService;

import javax.validation.Valid;

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

    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public PageResultDto<UserDro> getAllUser(@QuerydslPredicate(root = User.class) Predicate predicate,
                                             final Pageable pageable) throws StatusException {
        return new PageResultDto<>(userDao.findAll(predicate, pageable), UserDro.class);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.OK)
    public JsonNode createUser(@RequestBody @Valid UserDco userDco) {
        User user = userDaoService.create(userDco);
        return objectMapper.createObjectNode().put("id", user.getId());
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modifyUser(@PathVariable("userId") Long userId,
                           @RequestBody @Valid UserDuo userDuo) throws StatusException {
        userDaoService.modify(userId, userDuo);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUser(@PathVariable("userId") Long userId) throws StatusException {
        userDao.deleteById(userId);
    }
}
