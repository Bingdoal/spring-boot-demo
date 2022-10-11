package springboot.demo.utils;

import java.time.Duration;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.TimeZone;

public class TimeUtils {

  public static final ZoneOffset ZONE_OFFSET = Optional.of(ZoneId.systemDefault())
      .map(TimeZone::getTimeZone)
      .map(TimeZone::getRawOffset)
      .map(Duration::ofMillis)
      .map(Duration::getSeconds)
      .map(Number::intValue)
      .map(ZoneOffset::ofTotalSeconds)
      .orElse(null);
}
