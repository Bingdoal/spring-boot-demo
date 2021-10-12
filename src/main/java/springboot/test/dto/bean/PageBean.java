package springboot.test.dto.bean;

import lombok.Data;
import org.springframework.data.domain.Page;

@Data
public class PageBean {
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;

    PageBean(Page page){
        setPage(page.getPageable().getPageNumber() + 1);
        setSize(page.getPageable().getPageSize());
        setTotalElements(page.getTotalElements());
        setTotalPages(page.getTotalPages());
    }
}
