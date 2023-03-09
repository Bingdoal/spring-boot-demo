import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import springboot.demo.Application;

@SpringBootTest(classes = {Application.class})
@Slf4j
public class FunctionTest {
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Test
  public void test() {
    var encode = passwordEncoder.encode("123");
    var validation = passwordEncoder.matches("123",encode);
    log.info("encode: {},validation: {}",encode, validation);
    encode = passwordEncoder.encode("123");
    validation = passwordEncoder.matches("123",encode);
    log.info("encode: {},validation: {}",encode, validation);
  }
}
