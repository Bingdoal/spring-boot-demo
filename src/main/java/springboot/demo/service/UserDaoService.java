package springboot.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springboot.demo.dto.UserDto;
import springboot.demo.middleware.exception.RestException;
import springboot.demo.model.dao.UserDao;
import springboot.demo.model.entity.User;
import springboot.demo.utils.MyBeanUtils;

@Service
public class UserDaoService {

  @Autowired
  private UserDao userDao;
  @Autowired
  private PasswordEncoder passwordEncoder;

  public User create(UserDto userDto) {
    User user = new User();
    user.setName(userDto.getName());
    user.setPassword(passwordEncoder.encode(userDto.getPassword()));
    user.setEmail(userDto.getEmail());
    return userDao.save(user);
  }

  public User modify(Long userId, UserDto userDto) throws RestException {
    Optional<User> optUser = userDao.findById(userId);
    User user;
    if (optUser.isPresent()) {
      user = optUser.get();
    } else {
      throw new RestException(400, "UserId " + userId + " do not exist.");
    }
    MyBeanUtils.copyProperties(userDto, user, false);
    return userDao.save(user);
  }

  public List<User> findWithCondition(String name, String email) {
    Specification<User> spec = (root, query, builder) -> {
      List<Predicate> predicates = new ArrayList<>();
      if (name != null) {
        Expression<String> nameExp = root.get("name").as(String.class);
        predicates.add(builder.equal(nameExp, name));
      }
      if (email != null) {
        Expression<String> emailExp = root.get("email").as(String.class);
        predicates.add(builder.equal(emailExp, email));
      }
      query.where(builder.and(predicates.toArray(new Predicate[0])));
      return query.getRestriction();
    };
    return userDao.findAll(spec);
  }
}
