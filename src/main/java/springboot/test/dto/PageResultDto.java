package springboot.test.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageResultDto<T> {

    public PageResultDto(Page<T> pageResult) {
        this.setContent(pageResult.getContent());
        setPageable(new PageDto(pageResult));
    }

    private List<T> content;
    private PageDto pageable;
}
