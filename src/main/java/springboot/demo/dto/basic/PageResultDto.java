package springboot.demo.dto.basic;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import springboot.demo.middleware.exception.RestException;

import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
@NoArgsConstructor
public class PageResultDto<T> {

    public PageResultDto(Page<T> pageResult) {
        this.setContent(pageResult.getContent());
        setPageable(new PageDto(pageResult));
    }

    private List<T> content;
    private PageDto pageable;

    public PageResultDto(Page pageResult, Class<T> convertTo) throws RestException {
        List<T> list = new ArrayList<>();
        for (Object item : pageResult.getContent()) {
            list.add(convertObject(item, convertTo));
        }
        this.setContent(list);
        setPageable(new PageDto(pageResult));
    }


    private T convertObject(Object obj, Class<T> convertTo) throws RestException {
        try {
            T newObj = convertTo.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(obj, newObj);
            return newObj;
        } catch (Exception e) {
            throw new RestException(500, e.getMessage());
        }
    }
}
