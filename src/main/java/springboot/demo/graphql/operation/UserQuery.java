package springboot.demo.graphql.operation;

import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import springboot.demo.dto.basic.PageDto;
import springboot.demo.dto.basic.PageResultDto;
import springboot.demo.model.dao.UserDao;
import springboot.demo.model.entity.Post;
import springboot.demo.model.entity.QUser;
import springboot.demo.model.entity.User;

import java.util.Optional;

@Component
@Slf4j
public class UserQuery implements GraphQLQueryResolver {

    @Autowired
    private UserDao userDao;

    public Iterable<User> getUserList() {
        return userDao.findAll();
    }

    public User user(Long id) {
        Optional<User> userOpt = userDao.findOne(QUser.user.id.eq(id));
        return userOpt.orElse(null);
    }

    public Iterable<User> userSearch(User user) {
        return userDao.findAll(
                QUser.user.email.contains(user.getEmail()).and(
                        QUser.user.name.contains(user.getName())
                ));
    }

    public PageResultDto<User> pageUser(PageDto pageDto) {
        Pageable pageable = Pageable.ofSize(pageDto.getSize()).withPage(pageDto.getPage() - 1);
        return new PageResultDto<>(userDao.findAll(pageable));
    }
}
