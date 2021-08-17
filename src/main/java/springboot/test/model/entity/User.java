package springboot.test.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
public class User extends EntityBase {
    private String name;
    @JsonIgnore
    private String password;
    private String description;
}
