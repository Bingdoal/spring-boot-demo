package springboot.test.model;


import lombok.Data;

import javax.persistence.*;

@MappedSuperclass
@Data
public class EntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", unique = true, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    protected Long id;
}
