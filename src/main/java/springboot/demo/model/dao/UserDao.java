package springboot.demo.model.dao;

import com.querydsl.core.types.dsl.StringPath;
import javax.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import springboot.demo.model.entity.QUser;
import springboot.demo.model.entity.User;

public interface UserDao extends DaoBase<User, Long>, QuerydslBinderCustomizer<QUser> {

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("update User u set u.email=:email where u.name=:name")
  int updateEmail(String name, String email);

  @Modifying(clearAutomatically = true, flushAutomatically = true)
  @Query("update User u set u.name=:name where u.email=:email")
  int updateName(String name, String email);

  @Lock(LockModeType.PESSIMISTIC_WRITE)
  @Query("select u from User u Where u.name = :name")
  User findByNameWithLock(String name);

  @Override
  default void customize(final QuerydslBindings bindings, final QUser qEntity) {
    // 自定義綁定關係，使用白名單模式 (true)，只有明確列出的欄位才適用於搜尋
    bindings.excludeUnlistedProperties(true);
    bindings.including(qEntity.name, qEntity.email);
    bindings.bind(String.class).first((StringPath field, String value) -> {
      if (value.equals("NULL")) {
        return field.isNull();
      } else {
        return field.containsIgnoreCase(value);
      }
    });
  }
}
