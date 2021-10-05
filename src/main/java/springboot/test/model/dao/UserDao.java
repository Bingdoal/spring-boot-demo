package springboot.test.model.dao;

import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import springboot.test.model.entity.QUser;
import springboot.test.model.entity.User;

public interface UserDao extends DaoBase<User, Long>, QuerydslBinderCustomizer<QUser> {

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
