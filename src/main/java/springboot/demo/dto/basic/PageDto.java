package springboot.demo.dto.basic;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@NoArgsConstructor
@Data
public class PageDto {
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;

    PageDto(Page page) {
        setPage(page.getPageable().getPageNumber() + 1);
        setSize(page.getPageable().getPageSize());
        setTotalElements(page.getTotalElements());
        setTotalPages(page.getTotalPages());
    }
//
//    public int getSize() {
//        if (size <= 0) {
//            return 10;
//        }
//        return size;
//    }
//
//    public int getPage() {
//        if (page <= 0) {
//            return 1;
//        }
//        return page;
//    }
}
