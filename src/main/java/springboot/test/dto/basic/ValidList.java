package springboot.test.dto.basic;

import lombok.Getter;
import lombok.experimental.Delegate;

import javax.validation.Valid;
import java.util.*;

@Getter
public class ValidList<E> implements List<E> {
    @Valid
    @Delegate
    private final List<E> list;

    public ValidList() {
        this.list = new ArrayList<E>();
    }

    public ValidList(List<E> list) {
        this.list = list;
    }
}
