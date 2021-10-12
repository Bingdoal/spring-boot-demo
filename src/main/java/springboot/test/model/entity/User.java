package springboot.test.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Entity;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@JsonIgnoreProperties("password")
public class User extends EntityBase {
    private String name;
    private String password;
    private String email;
}
