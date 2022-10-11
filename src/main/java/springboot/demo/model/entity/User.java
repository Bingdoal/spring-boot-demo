package springboot.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.Entity;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@JsonIgnoreProperties("password")
@Accessors(chain = true)
public class User extends EntityBase {
    private String name;
    private String password;
    private String email;
}
