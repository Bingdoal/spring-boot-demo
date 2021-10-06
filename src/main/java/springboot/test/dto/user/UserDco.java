package springboot.test.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserDco {
    @NotNull
    @NotBlank
    @Size(max = 255, message = "{validation.user.name.length.max}")
    private String name;
    @NotNull
    @NotBlank
    private String password;
    @NotNull
    @NotBlank
    @Size(max = 255, message = "{validation.user.email.length.max}")
    private String email;
}
