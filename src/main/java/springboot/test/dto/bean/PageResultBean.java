package springboot.test.dto.bean;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import springboot.test.middleware.exception.StatusException;

import java.util.ArrayList;
import java.util.List;

@Data
@Slf4j
public class PageResultBean<T> {

    public PageResultBean(Page<T> pageResult) {
        this.setContent(pageResult.getContent());
        setPageable(new PageBean(pageResult));
    }

    public PageResultBean(Page pageResult, Class<T> convertTo) throws StatusException {
        List<T> list = new ArrayList<>();
        for (Object item : pageResult.getContent()) {
            list.add(convertObject(item, convertTo));
        }
        this.setContent(list);
        setPageable(new PageBean(pageResult));
    }

    private List<T> content;
    private PageBean pageable;

    private T convertObject(Object obj, Class<T> convertTo) throws StatusException {
        try {
            T newObj = convertTo.getDeclaredConstructor().newInstance();
            BeanUtils.copyProperties(obj, newObj);
            return newObj;
        } catch (Exception e) {
            throw new StatusException(500, e.getMessage());
        }
    }
}
