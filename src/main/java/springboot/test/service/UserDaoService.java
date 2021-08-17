package springboot.test.service;

import com.querydsl.core.types.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import springboot.test.model.dao.UserDao;
import springboot.test.model.entity.QUser;
import springboot.test.model.entity.User;

@Service
public class UserDaoService {
    @Autowired
    private UserDao userDao;

    private Page<User> findNameContain(String name, Pageable pageable) {
        Predicate predicate = QUser.user.name.contains(name);
        return userDao.findAll(predicate, pageable);
    }
}
