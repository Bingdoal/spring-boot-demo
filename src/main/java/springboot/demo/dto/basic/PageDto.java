package springboot.demo.dto.basic;

import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class PageDto {
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;

    PageDto(Page page){
        setPage(page.getPageable().getPageNumber() + 1);
        setSize(page.getPageable().getPageSize());
        setTotalElements(page.getTotalElements());
        setTotalPages(page.getTotalPages());
    }
}
