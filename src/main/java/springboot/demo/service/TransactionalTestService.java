package springboot.demo.service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import springboot.demo.model.dao.PostDao;
import springboot.demo.model.dao.UserDao;

@Service
@Slf4j
public class TransactionalTestService {

  @Resource
  private EntityManager entityManager;
  @Resource
  private UserDao userDao;
  @Resource
  private PostDao postDao;

  @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
  public void test() throws Exception {
    var user = userDao.findByNameWithLock("transTestTTT");
    log.info("user: {}", user);
    user.setPassword("44235325");
    userDao.save(user);
    entityManager.flush();
    entityManager.clear();
    user = userDao.findByNameWithLock("transTestTTT");
    log.info("user: {}", user);
    throw new Exception("");
  }
}
