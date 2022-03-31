package springboot.demo.dto;

import lombok.Data;
import springboot.demo.validation.PasswordRule;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

@Data
public class UserDto {
    @NotBlank(groups = Create.class,message = "{NotBlank.userDto.name}")
    @Size(min = 2, max = 255, message = "{Size.userDto.name}")
    private String name;

    @NotBlank(groups = Create.class)
    @PasswordRule(groups = Create.class)
    @Null(groups = Update.class)
    private String password;

    @NotBlank(groups = Create.class)
    @Size(max = 255, groups = {Update.class, Create.class})
    @Email(message = "{Email.userDto.email}")
    private String email;

    public interface Create extends Default {
    }

    public interface Update extends Default {
    }
}
