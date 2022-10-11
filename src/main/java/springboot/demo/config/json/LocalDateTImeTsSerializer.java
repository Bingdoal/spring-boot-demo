package springboot.demo.config.json;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Objects;
import springboot.demo.utils.TimeUtils;

public class LocalDateTImeTsSerializer extends JsonSerializer<LocalDateTime> {

  @Override
  public void serialize(
      LocalDateTime localDateTime,
      JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider) throws IOException {
    if (Objects.nonNull(localDateTime)) {
      jsonGenerator.writeNumber(localDateTime.toInstant(TimeUtils.ZONE_OFFSET).toEpochMilli());
    } else {
      jsonGenerator.writeNumber(0L);
    }
  }
}
