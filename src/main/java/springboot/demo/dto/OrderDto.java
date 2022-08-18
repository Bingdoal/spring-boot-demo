package springboot.demo.dto;

import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderDto {
  @NotNull
  private Long userId;
  @NotNull
  private String item;
}
