package springboot.test.model.entity;


import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
public class EntityBase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", unique = true, nullable = false, columnDefinition = "BIGINT UNSIGNED")
    protected Long id;
    @CreationTimestamp
    protected LocalDateTime creationTime;
    @UpdateTimestamp
    protected LocalDateTime modificationTime;
}
