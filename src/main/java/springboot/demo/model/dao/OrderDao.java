package springboot.demo.model.dao;

import com.querydsl.core.types.dsl.StringPath;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.querydsl.binding.QuerydslBinderCustomizer;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import springboot.demo.model.entity.Order;
import springboot.demo.model.entity.QOrder;
import springboot.demo.utils.DaoUtils;

public interface OrderDao extends DaoBase<Order, Long>, QuerydslBinderCustomizer<QOrder> {

  @Override
  default void customize(final QuerydslBindings bindings, final QOrder qEntity) {
    bindings.excludeUnlistedProperties(true);
    bindings.including(qEntity.userId, qEntity.status);
    DaoUtils.filterBinding(bindings, qEntity);
  }
}
