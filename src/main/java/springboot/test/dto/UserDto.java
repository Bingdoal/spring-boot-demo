package springboot.test.dto;

import lombok.Data;
import springboot.test.validation.PasswordRule;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

@Data
public class UserDto {
    @NotBlank(groups = Create.class)
    @Size(max = 255)
    private String name;

    @NotBlank(groups = Create.class)
    @PasswordRule(groups = Create.class)
    @Null(groups = Update.class)
    private String password;

    @NotBlank(groups = Create.class)
    @Size(max = 255, groups = {Update.class, Create.class})
    @Email()
    private String email;

    public interface Create extends Default {
    }

    public interface Update extends Default {
    }
}
