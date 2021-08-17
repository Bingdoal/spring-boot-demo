package springboot.test.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class PageDto {
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private long totalElements;

    PageDto(Page page){
        setPageNumber(page.getPageable().getPageNumber() + 1);
        setPageSize(page.getPageable().getPageSize());
        setTotalElements(page.getTotalElements());
        setTotalPages(page.getTotalPages());
    }
}
