package springboot.demo.utils;

import com.querydsl.core.types.dsl.StringPath;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.data.querydsl.binding.QuerydslBindings;
import springboot.demo.model.entity.QOrder;

public class DaoUtils {

  public static void filterBinding(final QuerydslBindings bindings, final QOrder qEntity) {
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
