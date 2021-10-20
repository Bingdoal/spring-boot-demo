package springboot.test.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserDto {
    @NotBlank(groups = Create.class)
    @Size(max = 255, message = "{validation.user.name.length.max}")
    private String name;
    @NotBlank(groups = Create.class)
    private String password;
    @NotBlank(groups = Create.class)
    @Size(max = 255, message = "{validation.user.email.length.max}")
    @Email()
    private String email;
}
