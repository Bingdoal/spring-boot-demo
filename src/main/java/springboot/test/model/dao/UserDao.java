package springboot.test.model.dao;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import org.springframework.data.querydsl.binding.SingleValueBinding;
import springboot.test.model.entity.QUser;
import springboot.test.model.entity.User;

public interface UserDao extends DaoBase<User, Long>, QuerydslBinderCustomizer<QUser> {

    Page<User> findAll(Predicate predicate, Pageable pageable);

    @Override
    default void customize(final QuerydslBindings bindings, final QUser qEntity) {
        // 自定義綁定關係，使用白名单模式 (true)，只有明確列出的欄位才適用於搜尋
        bindings.excludeUnlistedProperties(true);
        bindings.including(qEntity.name, qEntity.description);
        bindings.bind(String.class).first((SingleValueBinding<StringPath, String>) StringExpression::containsIgnoreCase);
    }
}
