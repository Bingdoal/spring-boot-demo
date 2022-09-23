package springboot.demo.config.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Objects;

public class BigDecimalSerialize extends JsonSerializer<BigDecimal> {

  @Override
  public void serialize(
      BigDecimal bigDecimal,
      JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider
  ) throws IOException {
    if (Objects.nonNull(bigDecimal)) {
      jsonGenerator.writeString(bigDecimal.stripTrailingZeros().toPlainString());
    } else {
      jsonGenerator.writeString("");
    }
  }
}