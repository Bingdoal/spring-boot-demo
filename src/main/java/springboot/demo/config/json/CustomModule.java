package springboot.demo.config.json;

import com.fasterxml.jackson.databind.module.SimpleModule;
import java.math.BigDecimal;

public class CustomModule extends SimpleModule {

  public CustomModule() {
    addSerializer(BigDecimal.class, new BigDecimalSerialize());
    addDeserializer(String.class, new StringTrimDeserialize());
  }
}
