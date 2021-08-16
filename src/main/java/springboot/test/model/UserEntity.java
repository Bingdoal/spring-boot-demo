package springboot.test.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@Table(name = "user")
public class UserEntity extends EntityBase{
    private String name;
    private String password;
    private String description;
}
