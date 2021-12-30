package springboot.demo.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
public class PostDto {
    @NotNull
    @NotEmpty(groups = {Update.class})
    private String content;
    @NotNull
    @Null(groups = {Update.class})
    private Long authorId;

    public interface Update {
    }
}
