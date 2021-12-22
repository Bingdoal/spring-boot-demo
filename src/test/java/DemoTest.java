import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DemoTest {
    @Test
    public void oneEqualOne() {
        Assertions.assertEquals(1, 1);
    }

    @Test
    public void oneEqualTwo() {
        Assertions.assertNotEquals(1, 2);
    }
}
