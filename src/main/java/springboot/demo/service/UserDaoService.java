package springboot.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springboot.demo.dto.UserDto;
import springboot.demo.middleware.exception.StatusException;
import springboot.demo.model.dao.UserDao;
import springboot.demo.model.entity.User;
import springboot.demo.utils.MyBeanUtils;

import java.util.Optional;

@Service
public class UserDaoService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User create(UserDto userDco) {
        User user = new User();
        user.setName(userDco.getName());
        user.setPassword(passwordEncoder.encode(userDco.getPassword()));
        user.setEmail(userDco.getEmail());
        return userDao.save(user);
    }

    public User modify(Long userId, UserDto userDuo) throws StatusException {
        Optional<User> optUser = userDao.findById(userId);
        User user;
        if (optUser.isPresent()) {
            user = optUser.get();
        } else {
            throw new StatusException(400, "UserId " + userId + " do not exist.");
        }
        MyBeanUtils.copyProperties(userDuo, user, false);
        return userDao.save(user);
    }
}
