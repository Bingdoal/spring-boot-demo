package springboot.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springboot.test.dto.UserDto;
import springboot.test.exception.StatusException;
import springboot.test.model.dao.UserDao;
import springboot.test.model.entity.User;
import springboot.test.utils.MyBeanUtils;

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
