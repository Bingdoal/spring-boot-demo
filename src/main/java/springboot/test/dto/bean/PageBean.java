package springboot.test.dto.bean;

import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class PageBean {
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private long totalElements;

    PageBean(Page page){
        setPageNumber(page.getPageable().getPageNumber() + 1);
        setPageSize(page.getPageable().getPageSize());
        setTotalElements(page.getTotalElements());
        setTotalPages(page.getTotalPages());
    }
}
