package springboot.demo.graphql;

import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import springboot.demo.model.dao.UserDao;
import springboot.demo.model.entity.QUser;
import springboot.demo.model.entity.User;

import java.util.Collection;
import java.util.Optional;

@Component
@Slf4j
public class UserGQL implements GraphQLQueryResolver {

    @Autowired
    private UserDao userDao;

    public Collection<User> userList() {
        return userDao.findAll();
    }

    public User user(Long id) {
        Optional<User> userOpt = userDao.findOne(QUser.user.id.eq(id));
        return userOpt.orElse(null);
    }
}