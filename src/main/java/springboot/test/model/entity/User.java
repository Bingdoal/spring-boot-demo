package springboot.test.model.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class User extends EntityBase {
    private String name;
    private String password;
    private String email;
}
