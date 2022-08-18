package springboot.demo.dto.enums.base;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public abstract class ConverterBase<T extends Enum<T> & EnumBase<E>, E> implements
    AttributeConverter<T, E> {
  private final Class<T> clazz;

  public ConverterBase(Class<T> clazz) {
    this.clazz = clazz;
  }

  @Override
  public E convertToDatabaseColumn(T attribute) {
    return attribute != null ? attribute.getValue() : null;
  }

  @Override
  public T convertToEntityAttribute(E dbData) {
    T[] enums = clazz.getEnumConstants();
    for (T e : enums) {
      if (e.getValue().equals(dbData)) {
        return e;
      }
    }
    return null;
  }
}
