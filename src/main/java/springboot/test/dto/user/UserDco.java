package springboot.test.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class UserDco {
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private String password;
    @NotNull
    @NotBlank
    private String email;
}
