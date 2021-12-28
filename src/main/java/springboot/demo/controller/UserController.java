package springboot.demo.controller;

import com.querydsl.core.types.Predicate;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springboot.demo.utils.annotation.ApiPageable;
import springboot.demo.dto.basic.CreatedDto;
import springboot.demo.dto.UserDto;
import springboot.demo.dto.basic.I18nDto;
import springboot.demo.dto.basic.PageResultDto;
import springboot.demo.middleware.exception.StatusException;
import springboot.demo.model.dao.UserDao;
import springboot.demo.model.entity.User;
import springboot.demo.service.UserDaoService;

import java.util.Optional;

@Api(tags = "User")
@Slf4j
@RestController()
@RequestMapping("/v1/user")
public class UserController {

    @Autowired
    private UserDao userDao;
    @Autowired
    private UserDaoService userDaoService;

    @ApiPageable
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public PageResultDto<User> getAllUser(@QuerydslPredicate(root = User.class) Predicate predicate,
                                          final Pageable pageable) {
        return new PageResultDto<>(userDao.findAll(predicate, pageable));
    }

    @GetMapping("/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public User getOneUser(@PathVariable("userId") Long userId) throws StatusException {
        Optional<User> userOption = userDao.findById(userId);
        if (userOption.isEmpty()) {
            throw new StatusException(404, I18nDto.key("user.controller.not.found.by.id").args(userId));
        }
        return userOption.get();
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public CreatedDto createUser(@RequestBody @Validated(UserDto.Create.class) UserDto dto) {
        User user = userDaoService.create(dto);
        return new CreatedDto(user.getId());
    }

    @PutMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void modifyUser(@PathVariable("userId") Long userId,
                           @RequestBody @Validated(UserDto.Update.class) UserDto dto) throws StatusException {
        userDaoService.modify(userId, dto);
    }

    @DeleteMapping("/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUser(@PathVariable("userId") Long userId) {
        userDao.deleteById(userId);
    }
}
