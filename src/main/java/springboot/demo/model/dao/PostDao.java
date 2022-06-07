package springboot.demo.model.dao;

import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import springboot.demo.model.entity.Post;
import springboot.demo.model.entity.QPost;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface PostDao extends DaoBase<Post, Long>, QuerydslBinderCustomizer<QPost> {
    @Override
    default void customize(final QuerydslBindings bindings, final QPost qEntity) {
        // 自定義綁定關係，使用白名單模式 (true)，只有明確列出的欄位才適用於搜尋
        bindings.excludeUnlistedProperties(true);
        bindings.including(qEntity.content, qEntity.authorId);
        bindings.bind(String.class).first((StringPath field, String value) -> {
            if (value.equals("NULL")) {
                return field.isNull();
            } else {
                return field.containsIgnoreCase(value);
            }
        });
        bindings.bind(qEntity.creationTime).all((path, value) -> {
            List<? extends LocalDateTime> dates = new ArrayList<>(value);
            if (dates.size() == 1) {
                return Optional.of(path.eq(dates.get(0)));
            } else {
                return Optional.of(path.between(dates.get(0), dates.get(1)));
            }
        });
    }
}
