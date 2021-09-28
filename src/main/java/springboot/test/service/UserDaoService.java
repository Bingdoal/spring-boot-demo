package springboot.test.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springboot.test.dto.user.UserDco;
import springboot.test.dto.user.UserDuo;
import springboot.test.exception.StatusException;
import springboot.test.model.dao.UserDao;
import springboot.test.model.entity.User;

import java.util.Optional;

@Service
public class UserDaoService {
    @Autowired
    private UserDao userDao;

    public User create(UserDco userDco) {
        User user = new User();
        user.setName(userDco.getName());
        user.setPassword(userDco.getPassword());
        user.setEmail(userDco.getEmail());
        return userDao.save(user);
    }

    public User modify(Long userId, UserDuo userDuo) throws StatusException {
        Optional<User> optUser = userDao.findById(userId);
        User user;
        if (optUser.isPresent()) {
            user = optUser.get();
        } else {
            throw new StatusException(400, "UserId " + userId + " do not exist.");
        }
        if (userDuo.getName() != null) {
            user.setName(userDuo.getName());
        }
        if (userDuo.getPassword() != null) {
            user.setPassword(userDuo.getPassword());
        }
        if (userDuo.getEmail() != null) {
            user.setEmail(userDuo.getEmail());
        }
        return userDao.save(user);
    }
}
