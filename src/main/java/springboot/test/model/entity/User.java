package springboot.test.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
public class User extends EntityBase {
    private String name;
    private String password;
    private String email;
}
