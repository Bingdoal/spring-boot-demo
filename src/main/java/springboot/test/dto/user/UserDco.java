package springboot.test.dto.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class UserDco {
    @NotNull
    @NotBlank
    @Size(max = 255, message = "The length of field(name) must less then 255")
    private String name;
    @NotNull
    @NotBlank
    private String password;
    @NotNull
    @NotBlank
    @Size(max = 255, message = "The length of field(email) must less then 255")
    private String email;
}
