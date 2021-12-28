package springboot.demo.utils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.beans.FeatureDescriptor;
import java.util.stream.Stream;

public class MyBeanUtils {
    public static Object copyProperties(Object source, Object target) {
        return copyProperties(source, target, true);
    }

    public static Object copyProperties(Object source, Object target, boolean includeNull) {
        if (!includeNull) {
            BeanUtils.copyProperties(source, target, getNullPropertyNames(source));
        } else {
            BeanUtils.copyProperties(source, target);
        }
        return target;
    }

    public static String[] getNullPropertyNames(Object source) {
        final BeanWrapper wrappedSource = new BeanWrapperImpl(source);
        return Stream.of(wrappedSource.getPropertyDescriptors())
                .map(FeatureDescriptor::getName)
                .filter(propertyName -> wrappedSource.getPropertyValue(propertyName) == null)
                .toArray(String[]::new);
    }
}
