package springboot.demo.dto.enums;


import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import springboot.demo.dto.enums.base.ConverterBase;
import springboot.demo.dto.enums.base.EnumBase;

@AllArgsConstructor
public enum OrderStatus implements EnumBase<Integer> {
  SUCCESS(1),
  PROCESS(2),
  COMPLETE(3),
  CANCEL(-1);

  @JsonValue
  private final int value;

  @Override
  public Integer getValue() {
    return value;
  }


  public static class Converter extends ConverterBase<OrderStatus, Integer> {

    public Converter() {
      super(OrderStatus.class);
    }
  }
}