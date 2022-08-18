package springboot.demo.dto.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;
import springboot.demo.dto.enums.base.ConverterBase;
import springboot.demo.dto.enums.base.EnumBase;

@Getter
@AllArgsConstructor
public enum OrderStatus implements EnumBase<Integer> {
  SUCCESS(1),
  PROCESS(2),
  COMPLETE(3),
  CANCEL(-1);

  private final int value;

  public static class Converter extends ConverterBase<OrderStatus, Integer> {
    public Converter() {
      super(OrderStatus.class);
    }
  }
}