package springboot.demo.model.dao;

import com.querydsl.core.types.dsl.StringPath;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import springboot.demo.model.entity.Post;
import springboot.demo.model.entity.QPost;

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
    }
}
