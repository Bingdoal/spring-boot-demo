package springboot.demo.model.entity;

import javax.persistence.Convert;
import javax.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import springboot.demo.dto.enums.OrderStatus;
import springboot.demo.dto.enums.OrderStatus.Converter;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
public class Order extends EntityBase {

  private Long userId;
  private String item;
  @Convert(converter = Converter.class)
  private OrderStatus status;
}
