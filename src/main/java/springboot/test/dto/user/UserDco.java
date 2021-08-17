package springboot.test.dto.user;

import lombok.Data;
import springboot.test.model.entity.User;

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
    private String description;

    public User toUser() {
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        user.setDescription(description);
        return user;
    }
}
